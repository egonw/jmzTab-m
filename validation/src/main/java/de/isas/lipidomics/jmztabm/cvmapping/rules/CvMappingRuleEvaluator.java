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
package de.isas.lipidomics.jmztabm.cvmapping.rules;

import de.isas.mztab1_1.model.MzTab;
import de.isas.mztab1_1.model.ValidationMessage;
import info.psidev.cvmapping.CvMapping;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Leibniz-Institut für Analytische Wissenschaften – ISAS – e.V.
 */
public class CvMappingRuleEvaluator {

    private final MzTab mzTab;
    private final CvMapping mapping;

    public CvMappingRuleEvaluator(MzTab mzTab, CvMapping mapping) {
        this.mzTab = mzTab;
        this.mapping = mapping;
    }

    public List<ValidationMessage> evaluate() {
//        mapping.getCvMappingRuleList().getCvMappingRule().stream().map((cvRule) ->
//            {
//                List<CvTerm> allowedTerms = cvRule.getCvTerm();
//                
//            });
        return Collections.emptyList();
    }

}