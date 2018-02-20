/*
 * Copyright 2017 Nils Hoffmann &lt;nils.hoffmann@isas.de&gt;.
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
package uk.ac.ebi.pride.jmztab1_1.model;

import de.isas.mztab1_1.model.IndexedElement;

/**
 * <p>IMZTabColumn interface.</p>
 *
 * @author nilshoffmann
 * @since 1.1
 * 
 */
public interface IMZTabColumn {

    /**
     * Get the column data type Class.
     *
     * @return a {@link java.lang.Class} object.
     */
    Class<?> getDataType();

    /**
     * Indexed element used in optional column header and logical position
     * definition. In stable column, the return is null.
     *
     * Notice: this design pattern not fit for
     * {@link uk.ac.ebi.pride.jmztab1_1.model.AbundanceColumn}, {@link uk.ac.ebi.pride.jmztab1_1.model.OptionColumn} and
     * {@link ParameterOptionColumn}. These optional columns need be generated by
     * calling {@link uk.ac.ebi.pride.jmztab1_1.model.MZTabColumnFactory} 's methods.
     *
     * @see #getHeader()
     * @see #getLogicPosition()
     * @see #getHeader()
     * @see #getLogicPosition()
     * @return a {@link de.isas.mztab1_1.model.IndexedElement} object.
     */
    IndexedElement getElement();

    /**
     * Get the column name. For stable column, name and header are same. While
     * for optional column, name is part of its header. For example, optional
     * column which header is search_engine_score_ms_run[1-n], and its name is
     * search_engine_score. Besides this, ms_run[1-n] is kind of
     * {@link #getElement()}
     *
     * Notice: this design pattern not fit for
     * {@link uk.ac.ebi.pride.jmztab1_1.model.AbundanceColumn}, {@link uk.ac.ebi.pride.jmztab1_1.model.OptionColumn} and
     * {@link ParameterOptionColumn}. These optional columns need be generated by
     * calling {@link uk.ac.ebi.pride.jmztab1_1.model.MZTabColumnFactory} 's methods.
     *
     * @see #getName()
     * @see #setElement(IndexedElement)
     * @see #getName()
     * @see #setElement(IndexedElement)
     * @return a {@link java.lang.String} object.
     */
    String getHeader();

    /**
     * Get the column logical position. For stable column, order and logical
     * position are same. But for optional column, the logical position need to
     * calculate by concatenate order and index element id. For example,
     * optional column search_engine_score_ms_run[2] in Protein section, its
     * order is 09, and the logical position is 092. Because the element
     * ms_run[2] 's index is 2.
     *
     * <p>Notice: this design pattern not fit for
     * {@link uk.ac.ebi.pride.jmztab1_1.model.AbundanceColumn}, {@link uk.ac.ebi.pride.jmztab1_1.model.OptionColumn} and
     * {@link ParameterOptionColumn}. These optional columns need be generated by
     * calling {@link uk.ac.ebi.pride.jmztab1_1.model.MZTabColumnFactory} 's methods.</p>
     *
     * <p>
     * Notice: in {@link uk.ac.ebi.pride.jmztab1_1.model.MZTabColumnFactory}, we use logical position to
     * maintain the logical consistence with in {@link de.isas.mztab1_1.model.MzTab}. During the
     * process of parsing mzTab file, we create a mapping between physical
     * position and internal logical position.</p>
     *
     * @see #getOrder()
     * @return a {@link java.lang.String} object.
     */
    String getLogicPosition();

    /**
     * Get the column name. For stable column, name and header are same. But for
     * optional column, name is part of its header. For example, optional column
     * which header is search_engine_score_ms_run[1-n], and its name is
     * search_engine_score. Besides this, ms_run[1-n] is kind of
     * {@link #getElement()}
     *
     * Notice: this design pattern not fit for
     * {@link uk.ac.ebi.pride.jmztab1_1.model.AbundanceColumn}, {@link uk.ac.ebi.pride.jmztab1_1.model.OptionColumn} and
     * {@link ParameterOptionColumn}. These optional columns need be generated by
     * calling {@link uk.ac.ebi.pride.jmztab1_1.model.MZTabColumnFactory} 's methods.
     *
     * @see #getHeader()
     * @see #setElement(IndexedElement)
     * @see #getHeader()
     * @see #setElement(IndexedElement)
     * @return a {@link java.lang.String} object.
     */
    String getName();

    /**
     * Get the column internal order. For stable column, order and logical
     * position are same. But for optional column, the logical position need to
     * be calculated by concatenating order and index element id. For example,
     * optional column search_engine_score_ms_run[2] in Protein section, its
     * order is 09, and the logical position is 092. Because the element
     * ms_run[2] 's index is 2.
     *
     * Notice: this design pattern not fit for
     * {@link uk.ac.ebi.pride.jmztab1_1.model.AbundanceColumn}, {@link uk.ac.ebi.pride.jmztab1_1.model.OptionColumn} and
     * {@link ParameterOptionColumn}. These optional columns need be generated by
     * calling {@link uk.ac.ebi.pride.jmztab1_1.model.MZTabColumnFactory} 's methods.
     *
     * @see #getLogicPosition()
     * @return a {@link java.lang.String} object.
     */
    String getOrder();

    /**
     * Judge this column belong to stable column or optional column.
     *
     * @return a boolean.
     */
    boolean isOptional();

    /**
     * <p>setHeader.</p>
     *
     * @param header a {@link java.lang.String} object.
     */
    void setHeader(String header);

    /**
     * <p>setLogicPosition.</p>
     *
     * @param logicPosition a {@link java.lang.String} object.
     */
    void setLogicPosition(String logicPosition);

    /*
     * Allows to reassign the order in case the file doesn't follow the recommended order
     */
    /**
     * <p>setOrder.</p>
     *
     * @param order a {@link java.lang.String} object.
     */
    void setOrder(String order);

    /**
     * Indexed element used in optional column header and logical position
     * definition. In stable column, the return is null.
     *
     * Notice: this design pattern not fit for
     * {@link uk.ac.ebi.pride.jmztab1_1.model.AbundanceColumn}, {@link uk.ac.ebi.pride.jmztab1_1.model.OptionColumn} and
     * {@link ParameterOptionColumn}. These optional columns need be generated by
     * calling {@link uk.ac.ebi.pride.jmztab1_1.model.MZTabColumnFactory} 's methods.
     *
     * @see #getHeader()
     * @see #getLogicPosition()
     * @see #getHeader()
     * @see #getLogicPosition()
     * @param element SHOULD NOT set null.
     */
    void setElement(IndexedElement element);

}
