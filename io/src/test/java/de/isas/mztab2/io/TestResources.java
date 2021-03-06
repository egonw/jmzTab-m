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
package de.isas.mztab2.io;

import de.isas.mztab2.model.Metadata;
import de.isas.mztab2.model.MzTab;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.Assert;
import org.junit.rules.TemporaryFolder;
import uk.ac.ebi.pride.jmztab2.model.MZTabConstants;
import uk.ac.ebi.pride.jmztab2.utils.errors.MZTabErrorOverflowException;
import uk.ac.ebi.pride.jmztab2.utils.errors.MZTabErrorType;
import uk.ac.ebi.pride.jmztab2.utils.errors.MZTabException;

/**
 * Utility class to use for parsing of resources that exist in a temporary
 * folder.
 *
 * @see MzTabRawParserTest
 * @see MzTabWriterTest
 * 
 * @author nilshoffmann
 */
public class TestResources {

    public static final String MZTAB_VERSION_HEADER = Metadata.PrefixEnum.MTD + MZTabConstants.TAB_STRING + Metadata.Properties.mzTabVersion + MZTabConstants.TAB_STRING + MZTabConstants.VERSION_MZTAB_M + MZTabConstants.NEW_LINE;

    public static MzTab parseResource(TemporaryFolder tf, String resource,
        MZTabErrorType.Level level, Integer expectedErrors) throws URISyntaxException, IOException, MZTabException, MZTabErrorOverflowException {
        File testFile = new File(tf.getRoot(), resource);
        Assert.assertTrue(testFile.exists() && testFile.isFile());
        MzTabFileParser parser = new MzTabFileParser(testFile);
        parser.parse(System.err, level, 500);
        if (parser.getErrorList().
            size() != expectedErrors) {
            Assert.fail(parser.getErrorList().
                toString());
        }
        if (parser.getMZTabFile() == null) {
            Assert.fail(parser.getErrorList().
                toString());
        }
        return parser.getMZTabFile();
    }

}
