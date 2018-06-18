/* 
 * Copyright 2018 Leibniz-Institut für Analytische Wissenschaften – ISAS – e.V..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.isas.mztab2.validation;

import de.isas.lipidomics.mztab2.validation.Validator;
import de.isas.mztab2.cvmapping.CvMappingUtils;
import de.isas.mztab2.cvmapping.CvParameterLookupService;
import de.isas.mztab2.cvmapping.JxPathElement;
import de.isas.mztab2.cvmapping.RemoveUserParams;
import de.isas.mztab2.cvmapping.RuleEvaluationResult;
import de.isas.mztab2.model.MzTab;
import de.isas.mztab2.model.Parameter;
import de.isas.mztab2.model.ValidationMessage;
import de.isas.mztab2.validation.handlers.AndValidationHandler;
import de.isas.mztab2.validation.handlers.ExtraParametersValidationHandler;
import de.isas.mztab2.validation.handlers.OrValidationHandler;
import de.isas.mztab2.validation.handlers.ResolvingCvRuleHandler;
import de.isas.mztab2.validation.handlers.SharedParametersValidationHandler;
import de.isas.mztab2.validation.handlers.XorValidationHandler;
import info.psidev.cvmapping.CvMapping;
import info.psidev.cvmapping.CvMappingRule;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.lang3.tuple.Pair;
import uk.ac.ebi.pride.jmztab2.utils.errors.CrossCheckErrorType;
import uk.ac.ebi.pride.jmztab2.utils.errors.MZTabError;
import uk.ac.ebi.pride.utilities.ols.web.service.client.OLSClient;
import uk.ac.ebi.pride.utilities.ols.web.service.config.OLSWsConfig;

/**
 * Validator implementation that uses a provided xml mapping file with rules for
 * required, recommended and optional CV parameters to assert that an mzTab
 * follows these rules.
 *
 * @author nilshoffmann
 */
@Slf4j
public class CvMappingValidator implements Validator<MzTab> {

    private final CvMapping mapping;
    private final CvRuleHandler ruleHandler;
    private final boolean errorIfTermNotInRule;
    private final CvTermValidationHandler andHandler;
    private final CvTermValidationHandler orHandler;
    private final CvTermValidationHandler xorHandler;
    private final CvTermValidationHandler extraHandler;
    private final CvTermValidationHandler sharedHandler;
    private final RemoveUserParams cvTermSelectionHandler;

    public static CvMappingValidator of(File mappingFile,
        boolean errorIfTermNotInRule) throws JAXBException {
        OLSWsConfig config = new OLSWsConfig();
        OLSClient client = new OLSClient(config);
        CvParameterLookupService service = new CvParameterLookupService(client);
        return of(mappingFile, service, errorIfTermNotInRule);
    }

    public static CvMappingValidator of(File mappingFile,
        CvParameterLookupService client, boolean errorIfTermNotInRule) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(CvMapping.class);
        Unmarshaller u = jaxbContext.createUnmarshaller();
        CvMapping mapping = (CvMapping) u.unmarshal(mappingFile);
        return new CvMappingValidator(mapping, client, errorIfTermNotInRule);
    }

    public static CvMappingValidator of(URL mappingFile,
        boolean errorIfTermNotInRule) throws JAXBException {
        OLSWsConfig config = new OLSWsConfig();
        OLSClient client = new OLSClient(config);
        CvParameterLookupService service = new CvParameterLookupService(client);
        return of(mappingFile, service, errorIfTermNotInRule);
    }

    public static CvMappingValidator of(URL mappingFile,
        CvParameterLookupService client, boolean errorIfTermNotInRule) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(CvMapping.class);
        Unmarshaller u = jaxbContext.createUnmarshaller();
        CvMapping mapping = (CvMapping) u.unmarshal(mappingFile);
        return new CvMappingValidator(mapping, client, errorIfTermNotInRule);
    }

    public CvMappingValidator(CvMapping mapping, CvParameterLookupService client,
        boolean errorIfTermNotInRule) {
        this(mapping, new ResolvingCvRuleHandler(client), errorIfTermNotInRule);
    }
    
    public CvMappingValidator(CvMapping mapping, CvRuleHandler ruleHandler,
        boolean errorIfTermNotInRule) {
        this.mapping = mapping;
        this.ruleHandler = ruleHandler;
        this.errorIfTermNotInRule = errorIfTermNotInRule;
        this.andHandler = new AndValidationHandler();
        this.orHandler = new OrValidationHandler();
        this.xorHandler = new XorValidationHandler();
        this.extraHandler = new ExtraParametersValidationHandler();
        this.sharedHandler = new SharedParametersValidationHandler();
        this.cvTermSelectionHandler = new RemoveUserParams();
    }

    @Override
    public List<ValidationMessage> validate(MzTab mzTab) {
        final List<ValidationMessage> messages = new LinkedList<>();
        JXPathContext context = JXPathContext.newContext(mzTab);
        mapping.getCvMappingRuleList().
            getCvMappingRule().
            forEach((rule) ->
            {
                messages.addAll(handleRule(context, rule, errorIfTermNotInRule));
            });
        return messages;
    }

    private List<ValidationMessage> handleRule(JXPathContext context,
        CvMappingRule rule, boolean errorOnTermNotInRule) {
        String path = rule.getCvElementPath();
        List<Pair<Pointer, ? extends Parameter>> selection = JxPathElement.
            toList(context, path, Parameter.class);
        if (selection.isEmpty()) {
            log.debug(
                "Evaluating rule " + rule.getId() + " on " + rule.
                getCvElementPath() + " did not yield any selected elements!");
            switch (rule.getRequirementLevel()) {
                case MAY:
                    return Arrays.asList(new MZTabError(
                        CrossCheckErrorType.RulePointerObjectNullOptional, -1,
                        rule.getCvElementPath(), rule.getId(), "optional",
                        CvMappingUtils.niceToString(rule)).
                        toValidationMessage());
                case SHOULD:
                    return Arrays.asList(new MZTabError(
                        CrossCheckErrorType.RulePointerObjectNullRecommended, -1,
                        rule.getCvElementPath(), rule.getId(), "recommended",
                        CvMappingUtils.niceToString(rule)).
                        toValidationMessage());
                case MUST:
                    //The object "{0}" accessed by {1} is {2}, but was null or empty. Allowed terms are defined in {3}
                    return Arrays.asList(new MZTabError(
                        CrossCheckErrorType.RulePointerObjectNullRequired, -1,
                        rule.getCvElementPath(), rule.getId(), "required",
                        CvMappingUtils.niceToString(rule)).
                        toValidationMessage());
                default:
                    throw new IllegalArgumentException(
                    "Unknown requirement level value: " + rule.
                        getRequirementLevel()+ "! Supported are: " + Arrays.
                        toString(CvMappingRule.RequirementLevel.
                            values()));

            }
        }

        List<Pair<Pointer, ? extends Parameter>> filteredSelection = cvTermSelectionHandler.handleSelection(selection);

        // and logic means that ALL of the defined terms or their children MUST appear
        // we only compare valid CVParameters here, user Params (no cv accession), are not compared!
        // if combination logic is AND, child expansion needs to be disabled to avoid nonsensical combinations
        final List<ValidationMessage> messages = new ArrayList<>();
        
        RuleEvaluationResult result = ruleHandler.handleRule(rule, filteredSelection);

        switch (rule.getCvTermsCombinationLogic()) {
            case AND:
                messages.addAll(andHandler.handleParameters(result, errorOnTermNotInRule));
                break;
            case OR: // any of the terms or their children need to appear
                messages.addAll(orHandler.handleParameters(result, errorOnTermNotInRule));
                break;
            case XOR:
                messages.addAll(xorHandler.handleParameters(result, errorOnTermNotInRule));
                break;
            default:
                throw new IllegalArgumentException(
                    "Unknown combination logic value: " + rule.
                        getCvTermsCombinationLogic() + "! Supported are: " + Arrays.
                        toString(CvMappingRule.CvTermsCombinationLogic.
                            values()));
        }
//        messages.addAll(sharedHandler.handleParameters(result, errorOnTermNotInRule));
        messages.addAll(extraHandler.handleParameters(result, errorOnTermNotInRule));
        return messages.isEmpty() ? Collections.emptyList() : messages;
    }

}