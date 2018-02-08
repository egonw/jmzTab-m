package uk.ac.ebi.pride.jmztab1_1.model;

import de.isas.mztab1_1.model.Assay;
import de.isas.mztab1_1.model.MsRun;
import de.isas.mztab1_1.model.Parameter;
import de.isas.mztab1_1.model.StudyVariable;
import java.util.SortedMap;
import java.util.TreeMap;

import static uk.ac.ebi.pride.jmztab1_1.model.MZTabConstants.TAB;

/**
 * This is a static factory class which used to generate a couple of MZTabColumn objects, and organized them "logicalPosition, MZTabColumn"
 * pair.
 * <p/>
 * Currently, mzTab table including three kinds of columns:
 * <ol>
 * <li>
 * Stable column with stable order: header name, data type, logical position and order are stable in these columns.
 * All of them have been defined in {@link ProteinColumn}, {@link PeptideColumn}, {@link PSMColumn} and
 * {@link SmallMoleculeColumn}.
 * </li>
 * <li>
 * Optional column with stable order: column name, data type and order have defined in the {@link ProteinColumn},
 * {@link PeptideColumn}, {@link PSMColumn} and {@link SmallMoleculeColumn}. But header name, logical position
 * dynamically depend on {@link IndexedElement}. These optional column generated by
 * {@link #addOptionalColumn(MZTabColumn, MsRun)} method.
 * </li>
 * <li>
 * Optional columns which put the end of table-based section. There are three types of optional column:
 * {@link AbundanceColumn}, {@link OptionColumn} and {@link ParameterOptionColumn}, which always be added at the
 * end of table. These optional columns have not stable column name, data type or order. In this factory, we use
 * {@link #addAbundanceOptionalColumn(Assay)} to generate {@link AbundanceColumn}; use
 * {@link #addOptionalColumn(String, Class)} to create {@link OptionColumn}; and
 * use {@link #addOptionalColumn(Assay, Parameter, Class)} to create {@link ParameterOptionColumn}.
 * </li>
 * </ol>
 *
 * @author qingwei
 * @since 23/05/13
 */
public class MZTabColumnFactory {

    private SortedMap<String, IMZTabColumn> stableColumnMapping = new TreeMap<String, IMZTabColumn>();
    private SortedMap<String, IMZTabColumn> optionalColumnMapping = new TreeMap<String, IMZTabColumn>();
    private SortedMap<String, IMZTabColumn> abundanceColumnMapping = new TreeMap<String, IMZTabColumn>();
    private SortedMap<String, IMZTabColumn> columnMapping = new TreeMap<String, IMZTabColumn>();

    private Section section;

    private MZTabColumnFactory() {
    }

    /**
     * Retrieves the MZTabColumnFactory accordingly to the {@link #section}
     *
     * @param section SHOULD be {@link Section#Protein_Header}, {@link Section#Peptide_Header} {@link Section#PSM_Header}
     *                or {@link Section#Small_Molecule_Header}.
     */
    public static MZTabColumnFactory getInstance(Section section) {
        section = Section.toHeaderSection(section);

        if (section == null) {
            throw new IllegalArgumentException("Section should use Protein_Header, Peptide_Header, PSM_Header or Small_Molecule_Header.");
        }

        MZTabColumnFactory factory = new MZTabColumnFactory();
        factory.section = section;

        return factory;
    }

    /**
     * Get the table-based section. {@link Section#Protein_Header}, {@link Section#Peptide_Header} {@link Section#PSM_Header}
     * or {@link Section#Small_Molecule_Header}
     */
    public Section getSection() {
        return section;
    }

    /**
     * Get stable columns mapping. Key is logical position, and value is MZTabColumn object.
     * Stable column with stable order: header name, data type, logical position and order are stable in these columns.
     * All of them have been defined in {@link ProteinColumn}, {@link PeptideColumn}, {@link PSMColumn} and
     * {@link SmallMoleculeColumn}.
     */
    public SortedMap<String, IMZTabColumn> getStableColumnMapping() {
        return stableColumnMapping;
    }

    public void setStableColumnMapping(SortedMap<String, IMZTabColumn> stableColumnMapping) {
        this.stableColumnMapping = stableColumnMapping;
    }

    /**
     * Get all abundance columns. Key is logical position, and value is MZTabColumn object.
     * Abundance Columns always stay in the end of the table-based section.
     *
     * @see AbundanceColumn
     */
    public SortedMap<String, IMZTabColumn> getAbundanceColumnMapping() {
        return abundanceColumnMapping;
    }

    public void setAbundanceColumnMapping(SortedMap<String, IMZTabColumn> abundanceColumnMapping) {
        this.abundanceColumnMapping = abundanceColumnMapping;
    }

    /**
     * Get all optional columns, including option column with stable order and name, abundance columns, optional columns
     * and cv param optional columns. Key is logical position, and value is MZTabColumn object.
     *
     * @see AbundanceColumn
     * @see OptionColumn
     * @see ParameterOptionColumn
     */
    public SortedMap<String, IMZTabColumn> getOptionalColumnMapping() {
        return optionalColumnMapping;
    }

    public void setOptionalColumnMapping(SortedMap<String, IMZTabColumn> optionalColumnMapping) {
        this.optionalColumnMapping = optionalColumnMapping;
    }

    /**
     * Get all columns in the factory. In this class, we maintain the following constraint at any time:
     */
    public SortedMap<String, IMZTabColumn> getColumnMapping() {
        return columnMapping;
    }

    public void setColumnMapping(SortedMap<String, IMZTabColumn> columnMapping) {
        this.columnMapping = columnMapping;
    }

    /**
     * Generates the mandatory columns per section. This methods helps to create this columns by default.
     */
    public void addDefaultStableColumns() {

        //TODO: Should ProteinCoverage be a Stable Column?
        switch (section) {
//            case Protein_Header:
//                addStableColumn(this, ProteinColumn.ACCESSION);
//                addStableColumn(this, ProteinColumn.DESCRIPTION);
//                addStableColumn(this, ProteinColumn.TAXID);
//                addStableColumn(this, ProteinColumn.SPECIES);
//                addStableColumn(this, ProteinColumn.DATABASE);
//                addStableColumn(this, ProteinColumn.DATABASE_VERSION);
//                addStableColumn(this, ProteinColumn.SEARCH_ENGINE);
//                addStableColumn(this, ProteinColumn.AMBIGUITY_MEMBERS);
//                addStableColumn(this, ProteinColumn.MODIFICATIONS);
//                addStableColumn(this, ProteinColumn.PROTEIN_COVERAGE);
//                break;
//            case Peptide_Header:
//                addStableColumn(this, PeptideColumn.SEQUENCE);
//                addStableColumn(this, PeptideColumn.ACCESSION);
//                addStableColumn(this, PeptideColumn.UNIQUE);
//                addStableColumn(this, PeptideColumn.DATABASE);
//                addStableColumn(this, PeptideColumn.DATABASE_VERSION);
//                addStableColumn(this, PeptideColumn.SEARCH_ENGINE);
//                addStableColumn(this, PeptideColumn.MODIFICATIONS);
//                addStableColumn(this, PeptideColumn.RETENTION_TIME);
//                addStableColumn(this, PeptideColumn.RETENTION_TIME_WINDOW);
//                addStableColumn(this, PeptideColumn.CHARGE);
//                addStableColumn(this, PeptideColumn.MASS_TO_CHARGE);
//                addStableColumn(this, PeptideColumn.SPECTRA_REF);
//                break;
//            case PSM_Header:
//                addStableColumn(this, PSMColumn.SEQUENCE);
//                addStableColumn(this, PSMColumn.PSM_ID);
//                addStableColumn(this, PSMColumn.ACCESSION);
//                addStableColumn(this, PSMColumn.UNIQUE);
//                addStableColumn(this, PSMColumn.DATABASE);
//                addStableColumn(this, PSMColumn.DATABASE_VERSION);
//                addStableColumn(this, PSMColumn.SEARCH_ENGINE);
//                addStableColumn(this, PSMColumn.MODIFICATIONS);
//                addStableColumn(this, PSMColumn.RETENTION_TIME);
//                addStableColumn(this, PSMColumn.CHARGE);
//                addStableColumn(this, PSMColumn.EXP_MASS_TO_CHARGE);
//                addStableColumn(this, PSMColumn.CALC_MASS_TO_CHARGE);
//                addStableColumn(this, PSMColumn.SPECTRA_REF);
//                addStableColumn(this, PSMColumn.PRE);
//                addStableColumn(this, PSMColumn.POST);
//                addStableColumn(this, PSMColumn.START);
//                addStableColumn(this, PSMColumn.END);
//                break;
            case Small_Molecule_Header:
                addStableColumns(this, SmallMoleculeColumn.Stable.values());
                break;
            case Small_Molecule_Feature_Header:
                addStableColumns(this, SmallMoleculeFeatureColumn.Stable.values());
                break;
            case Small_Molecule_Evidence_Header:
                addStableColumns(this, SmallMoleculeEvidenceColumn.Stable.values());
                break;
            default:
                throw new IllegalArgumentException("Can not use Comment, Metadata section.");

        }
    }
    
    private static void addStableColumns(MZTabColumnFactory factory, IMZTabColumn[] columns) {
        for(IMZTabColumn column:columns) {
            addStableColumn(factory, column);
        }
    }

    private static void addStableColumn(MZTabColumnFactory factory, IMZTabColumn column) {
        factory.stableColumnMapping.put(column.getLogicPosition(), column);
        factory.columnMapping.put(column.getLogicPosition(), column);
    }

    /**
     * Add a optional column which has stable order and name, into {@link #optionalColumnMapping} and {@link #columnMapping}.
     *
     * @param column SHOULD NOT set null.
     * @param msRun  SHOULD NOT set null.
     * @throws IllegalArgumentException: If user would like to add duplicate optional columns.
     * @see MZTabColumn#createOptionalColumn(Section, MZTabColumn, Integer, IndexedElement)
     */
    public void addOptionalColumn(IMZTabColumn column, MsRun msRun) {
        String position = column.getLogicPosition();
        if (columnMapping.containsKey(position)) {
            throw new IllegalArgumentException("There exists column " + columnMapping.get(position) + " in position " + position);
        }

        IMZTabColumn newColumn = null;

//        if (section == Section.Protein_Header) {
//            if (column.getName().equals(ProteinColumn.NUM_PSMS.getName()) ||
//                    column.getName().equals(ProteinColumn.NUM_PEPTIDES_DISTINCT.getName()) ||
//                    column.getName().equals(ProteinColumn.NUM_PEPTIDES_UNIQUE.getName())) {
//                newColumn = MZTabColumn.createOptionalColumn(section, column, null, msRun);
//            }
//        }

        if (newColumn != null) {
            optionalColumnMapping.put(newColumn.getLogicPosition(), newColumn);
            columnMapping.put(newColumn.getLogicPosition(), newColumn);
        }
    }

    /**
     * Add the best {@link SearchEngineScore} across all replicates reported into {@link #optionalColumnMapping} and
     * {@link #columnMapping}. This column is not available for the PSM section
     *
     * @param column best search_engine_score column to add. SHOULD NOT set null.
     * @param id of the {section}_search_engine_score[id] param defined in {@link Metadata} for this column. SHOULD NOT set null.
     *
     * The header of the column will be represented as: best_search_engine_score[id]
     */
    public void addBestSearchEngineScoreOptionalColumn(IMZTabColumn column, Integer id) {
        String position = column.getLogicPosition();
        if (columnMapping.containsKey(position)) {
            throw new IllegalArgumentException("There exists column " + columnMapping.get(position) + " in position " + position);
        }

        IMZTabColumn newColumn = null;

        switch (section) {
//            case Protein_Header:
//                if (column.getName().equals(ProteinColumn.BEST_SEARCH_ENGINE_SCORE.getName())) {
//                    newColumn = MZTabColumn.createOptionalColumn(section, column, id, null);
//                }
//                break;
//            case Peptide_Header:
//                if (column.getName().equals(PeptideColumn.BEST_SEARCH_ENGINE_SCORE.getName())) {
//                    newColumn = MZTabColumn.createOptionalColumn(section, column, id, null);
//                }
//                break;
//            case Small_Molecule_Header:
//                if (column.getName().equals(SmallMoleculeColumn.BEST_SEARCH_ENGINE_SCORE.getName())) {
//                    newColumn = MZTabColumn.createOptionalColumn(section, column, id, null);
//                }
//                break;
//            
            // TODO add a warning for PSMs, combination not possible
        }

        if (newColumn != null) {
            optionalColumnMapping.put(newColumn.getLogicPosition(), newColumn);
            columnMapping.put(newColumn.getLogicPosition(), newColumn);
        }
    }

    /** Add {@link SearchEngineScore} followed by {@link MsRun} (MsRun will be null in the PSM section) which has stable order and name,
     * into {@link #optionalColumnMapping} and {@link #columnMapping}.
     *
     * @param column search_engine_score column to add. SHOULD NOT set null.
     * @param id of the {section}_search_engine_score[id] param defined in {@link Metadata} for this column. SHOULD NOT set null.
     * @param msRun {@link MsRun} for this search_engine_score
     *
     * The header will be represented as: search_engine_score[id]{_ms_run[1-n]}
     */
    public void addSearchEngineScoreOptionalColumn(IMZTabColumn column, Integer id, MsRun msRun) {
        String position = column.getLogicPosition();
        if (columnMapping.containsKey(position)) {
            throw new IllegalArgumentException("There exists column " + columnMapping.get(position) + " in position " + position);
        }

        IMZTabColumn newColumn = null;

        switch (section) {
//            case Protein_Header:
//                if (column.getName().equals(ProteinColumn.SEARCH_ENGINE_SCORE.getName())) {
//                    newColumn = MZTabColumn.createOptionalColumn(section, column, id, msRun);
//                }
//                break;
//            case Peptide_Header:
//                if (column.getName().equals(PeptideColumn.SEARCH_ENGINE_SCORE.getName())) {
//                    newColumn = MZTabColumn.createOptionalColumn(section, column, id, msRun);
//                }
//                break;
//            case Small_Molecule_Header:
//                if (column.getName().equals(SmallMoleculeColumn.SEARCH_ENGINE_SCORE.getName())) {
//                    newColumn = MZTabColumn.createOptionalColumn(section, column, id, msRun);
//                }
//                break;
//            case PSM_Header:
//                if (column.getName().equals(PSMColumn.SEARCH_ENGINE_SCORE.getName())) {
//                    newColumn = MZTabColumn.createOptionalColumn(section, column, id, null);
//                }
//                break;
        }

        if (newColumn != null) {
            optionalColumnMapping.put(newColumn.getLogicPosition(), newColumn);
            columnMapping.put(newColumn.getLogicPosition(), newColumn);
        }
    }


    /**
     * Extract the order from logical position. Normally, the order is coming from top two characters of logical position.
     * For example, logical position is 092, then the order number is 9.
     */
    private String getColumnOrder(String position) {
        return position.substring(0, 2);
    }

    /**
     * Add {@link ProteinColumn#GO_TERMS} into {@link #optionalColumnMapping} and {@link #columnMapping}.
     * <p/>
     * Notice: this function only used in {@link Section#Protein_Header}
     */
    public void addGoTermsOptionalColumn() {
        if (section != Section.Protein_Header) {
            throw new IllegalArgumentException("go_terms optional column only add into the protein section.");
        }

//        MZTabColumn column = ProteinColumn.GO_TERMS;
//        optionalColumnMapping.put(column.getLogicPosition(), column);
//        columnMapping.put(column.getLogicPosition(), column);
    }

    /**
     * Add Reliability optional column into {@link #optionalColumnMapping} and {@link #columnMapping}.
     */
    public void addReliabilityOptionalColumn() {
        IMZTabColumn column = null;
        switch (section) {
//            case Protein_Header:
//                column = ProteinColumn.RELIABILITY;
//                break;
//            case Peptide_Header:
//                column = PeptideColumn.RELIABILITY;
//                break;
            case Small_Molecule_Header:
                column = SmallMoleculeColumn.Stable.RELIABILITY;
                break;
//            case PSM_Header:
//                column = PSMColumn.RELIABILITY;
//                break;
        }

        if (column != null) {
            columnMapping.put(column.getLogicPosition(), column);
            optionalColumnMapping.put(column.getLogicPosition(), column);
        }
    }

    public void addReliabilityColumn(String order) {
        IMZTabColumn column = null;
        switch (section) {
//            case Protein_Header:
//                column = ProteinColumn.RELIABILITY;
//                break;
//            case Peptide_Header:
//                column = PeptideColumn.RELIABILITY;
//                break;
            case Small_Molecule_Header:
                column = SmallMoleculeColumn.Stable.RELIABILITY;
                break;
//            case PSM_Header:
//                column = PSMColumn.RELIABILITY;
//                break;
        }

        if (column != null) {
            column.setOrder(order);
            optionalColumnMapping.put(column.getLogicPosition(), column);
            columnMapping.put(column.getLogicPosition(), column);
        }
    }

    /**
     * Add URI optional column into {@link #optionalColumnMapping} and {@link #columnMapping}.
     */
    public void addURIColumn() {
        IMZTabColumn column = null;
        switch (section) {
//            case Protein_Header:
//                column = ProteinColumn.URI;
//                break;
//            case Peptide_Header:
//                column = PeptideColumn.URI;
//                break;
            case Small_Molecule_Header:
                column = SmallMoleculeColumn.Stable.URI;
                break;
//            case PSM_Header:
//                column = PSMColumn.URI;
//                break;
        }

        if (column != null) {
            optionalColumnMapping.put(column.getLogicPosition(), column);
            columnMapping.put(column.getLogicPosition(), column);
        }
    }

    public void addURIOptionalColumn(String order) {
        IMZTabColumn column = null;
        switch (section) {
//            case Protein_Header:
//                column = ProteinColumn.URI;
//                break;
//            case Peptide_Header:
//                column = PeptideColumn.URI;
//                break;
            case Small_Molecule_Header:
                column = SmallMoleculeColumn.Stable.URI;
                break;
//            case PSM_Header:
//                column = PSMColumn.URI;
//                break;
        }

        if (column != null) {
            column.setOrder(order);
            optionalColumnMapping.put(column.getLogicPosition(), column);
            columnMapping.put(column.getLogicPosition(), column);
        }
    }

    private String addOptionColumn(IMZTabColumn column) {

        optionalColumnMapping.put(column.getLogicPosition(), column);
        columnMapping.put(column.getLogicPosition(), column);

        return column.getLogicPosition();
    }

    private String addOptionColumn(IMZTabColumn column, String order) {

        column.setOrder(order);
        optionalColumnMapping.put(column.getLogicPosition(), column);
        columnMapping.put(column.getLogicPosition(), column);

        return column.getLogicPosition();
    }

    /**
     * Add global {@link OptionColumn} into {@link #optionalColumnMapping} and {@link #columnMapping}.
     * The header like: opt_global_{name}
     *
     * @param name       SHOULD NOT empty.
     * @param columnType SHOULD NOT empty.
     */
    public String addOptionalColumn(String name, Class columnType) {
        IMZTabColumn column = new OptionColumn(null, name, columnType, new Integer(getColumnOrder(columnMapping.lastKey())));
        return addOptionColumn(column);
    }

    public String addOptionalColumn(String name, Class columnType, String order) {
        IMZTabColumn column = new OptionColumn(null, name, columnType, new Integer(getColumnOrder(columnMapping.lastKey())));
        return addOptionColumn(column, order);
    }

    /**
     * Add {@link OptionColumn} followed by assay into {@link #optionalColumnMapping} and {@link #columnMapping}.
     * The header like: opt_assay[1]_{name}
     *
     * @param assay      SHOULD NOT empty.
     * @param name       SHOULD NOT empty.
     * @param columnType SHOULD NOT empty.
     */
    public String addOptionalColumn(Assay assay, String name, Class columnType) {
        IMZTabColumn column = new OptionColumn(assay, name, columnType, new Integer(getColumnOrder(columnMapping.lastKey())));
        return addOptionColumn(column);
    }

    public String addOptionalColumn(Assay assay, String name, Class columnType, String order) {
        IMZTabColumn column = new OptionColumn(assay, name, columnType, new Integer(getColumnOrder(columnMapping.lastKey())));
        return addOptionColumn(column, order);
    }

    /**
     * Add {@link OptionColumn} followed by study variable into {@link #optionalColumnMapping} and {@link #columnMapping}.
     * The header like: opt_study_variable[1]_{name}
     *
     * @param studyVariable SHOULD NOT empty.
     * @param name          SHOULD NOT empty.
     * @param columnType    SHOULD NOT empty.
     */
    public String addOptionalColumn(StudyVariable studyVariable, String name, Class columnType) {
        IMZTabColumn column = new OptionColumn(studyVariable, name, columnType, new Integer(getColumnOrder(columnMapping.lastKey())));
        return addOptionColumn(column);
    }

    public String addOptionalColumn(StudyVariable studyVariable, String name, Class columnType, String order) {
        IMZTabColumn column = new OptionColumn(studyVariable, name, columnType, new Integer(getColumnOrder(columnMapping.lastKey())));
        return addOptionColumn(column, order);
    }

    /**
     * Add {@link OptionColumn} followed by ms_run into {@link #optionalColumnMapping} and {@link #columnMapping}.
     * The header like: opt_ms_run[1]_{name}
     *
     * @param msRun      SHOULD NOT empty.
     * @param name       SHOULD NOT empty.
     * @param columnType SHOULD NOT empty.
     */
    public String addOptionalColumn(MsRun msRun, String name, Class columnType) {
        IMZTabColumn column = new OptionColumn(msRun, name, columnType, new Integer(getColumnOrder(columnMapping.lastKey())));
        return addOptionColumn(column);
    }

    public String addOptionalColumn(MsRun msRun, String name, Class columnType, String order) {
        IMZTabColumn column = new OptionColumn(msRun, name, columnType, new Integer(getColumnOrder(columnMapping.lastKey())));
        return addOptionColumn(column, order);
    }

    /**
     * Add global {@link ParameterOptionColumn} into {@link #optionalColumnMapping} and {@link #columnMapping}.
     * The header like: opt_global_cv_{accession}_{parameter name}
     *
     * @param param      SHOULD NOT empty.
     * @param columnType SHOULD NOT empty.
     */
    public String addOptionalColumn(Parameter param, Class columnType) {
        IMZTabColumn column = new ParameterOptionColumn(null, param, columnType, new Integer(getColumnOrder(columnMapping.lastKey())));
        return addOptionColumn(column);
    }


    public String addOptionalColumn(Parameter param, Class columnType, String order) {
        IMZTabColumn column = new ParameterOptionColumn(null, param, columnType, new Integer(getColumnOrder(columnMapping.lastKey())));
        return addOptionColumn(column, order);
    }

    /**
     * Add {@link ParameterOptionColumn} followed by assay into {@link #optionalColumnMapping} and {@link #columnMapping}.
     * The header like: opt_assay[1]_cv_{accession}_{parameter name}
     *
     * @param assay      SHOULD NOT empty.
     * @param param      SHOULD NOT empty.
     * @param columnType SHOULD NOT empty.
     */
    public String addOptionalColumn(Assay assay, Parameter param, Class columnType) {
        IMZTabColumn column = new ParameterOptionColumn(assay, param, columnType, new Integer(getColumnOrder(columnMapping.lastKey())));
        return addOptionColumn(column);
    }

    public String addOptionalColumn(Assay assay, Parameter param, Class columnType, String order) {
        IMZTabColumn column = new ParameterOptionColumn(assay, param, columnType, new Integer(getColumnOrder(columnMapping.lastKey())));
        return addOptionColumn(column, order);
    }
    /**
     * Add {@link ParameterOptionColumn} followed by study variable into {@link #optionalColumnMapping} and {@link #columnMapping}.
     * The header like: opt_study_variable[1]_cv_{accession}_{parameter name}
     *
     * @param studyVariable SHOULD NOT empty.
     * @param param         SHOULD NOT empty.
     * @param columnType    SHOULD NOT empty.
     */
    public String addOptionalColumn(StudyVariable studyVariable, Parameter param, Class columnType) {
        IMZTabColumn column = new ParameterOptionColumn(studyVariable, param, columnType, new Integer(getColumnOrder(columnMapping.lastKey())));
        return addOptionColumn(column);
    }

    public String addOptionalColumn(StudyVariable studyVariable, Parameter param, Class columnType, String order) {
        IMZTabColumn column = new ParameterOptionColumn(studyVariable, param, columnType, new Integer(getColumnOrder(columnMapping.lastKey())));
        return addOptionColumn(column, order);
    }

    /**
     * Add {@link ParameterOptionColumn} followed by ms_run into {@link #optionalColumnMapping} and {@link #columnMapping}.
     * The header like: opt_ms_run[1]_cv_{accession}_{parameter name}
     *
     * @param msRun      SHOULD NOT empty.
     * @param param      SHOULD NOT empty.
     * @param columnType SHOULD NOT empty.
     */
    public String addOptionalColumn(MsRun msRun, Parameter param, Class columnType) {
        IMZTabColumn column = new ParameterOptionColumn(msRun, param, columnType, new Integer(getColumnOrder(columnMapping.lastKey())));
        return addOptionColumn(column);
    }

    public String addOptionalColumn(MsRun msRun, Parameter param, Class columnType, String order) {
        IMZTabColumn column = new ParameterOptionColumn(msRun, param, columnType, new Integer(getColumnOrder(columnMapping.lastKey())));
        return addOptionColumn(column, order);
    }

    /**
     * Add {@link AbundanceColumn} into {@link AbundanceColumn}, {@link #optionalColumnMapping} and {@link #columnMapping}.
     * The header like: {Section}_abundance_assay[1]
     *
     * @param assay SHOULD NOT empty.
     * @see AbundanceColumn#createOptionalColumn(Section, Assay, int)
     */
    public String addAbundanceOptionalColumn(Assay assay) {
        IMZTabColumn column = AbundanceColumn.createOptionalColumn(section, assay, new Integer(getColumnOrder(columnMapping.lastKey())));
        abundanceColumnMapping.put(column.getLogicPosition(), column);
        return addOptionColumn(column);
    }

    public String addAbundanceOptionalColumn(Assay assay, String order) {
        IMZTabColumn column = AbundanceColumn.createOptionalColumn(section, assay, new Integer(order));
        abundanceColumnMapping.put(column.getLogicPosition(), column);
        return addOptionColumn(column, order);
    }

    /**
     * Add three {@link AbundanceColumn} into {@link AbundanceColumn}, {@link #optionalColumnMapping} and {@link #columnMapping} at one time.
     * The header like: {Section}_abundance_study_variable[1], {Section}_abundance_stdev_study_variable[1],
     * {Section}_abundance_std_error_study_variable[1].
     *
     * @param studyVariable SHOULD NOT empty.
     * @see AbundanceColumn#createOptionalColumns(Section, StudyVariable, String)
     */
    public String addAbundanceOptionalColumn(StudyVariable studyVariable) {
        SortedMap<String, MZTabColumn> columns = AbundanceColumn.createOptionalColumns(section, studyVariable, new Integer(getColumnOrder(columnMapping.lastKey())));
        abundanceColumnMapping.putAll(columns);
        optionalColumnMapping.putAll(columns);
        columnMapping.putAll(columns);
        return columns.lastKey();
    }

    public String addAbundanceOptionalColumn(StudyVariable studyVariable, String order) {
        SortedMap<String, MZTabColumn> columns = AbundanceColumn.createOptionalColumns(section, studyVariable, order);
        abundanceColumnMapping.putAll(columns);
        optionalColumnMapping.putAll(columns);
        columnMapping.putAll(columns);
        return columns.lastKey();
    }
    
    public String addIdConfidenceMeasureColumn(Parameter parameter,
        Integer index, Class columnType) {
        if (section!=Section.Small_Molecule_Evidence_Header && section!=Section.Small_Molecule_Evidence) {
            throw new IllegalArgumentException("Section should be SmallMoleculeEvidence, but is "+section.getName());
        }
        if (parameter == null) {
            throw new NullPointerException("Parameter should not be null!");
        }

        SortedMap<String, MZTabColumn> columns = new TreeMap<String, MZTabColumn>();

        MZTabColumn column = new MZTabColumn("id_confidence_measure", columnType, false, new Integer(getColumnOrder(columnMapping.lastKey()))+"", index);
        
        columns.put(column.getLogicPosition(), column);
        optionalColumnMapping.putAll(columns);
        columnMapping.putAll(columns);
        return columns.lastKey();
    }

    /**
     * @return tab split column header string list.
     */
    public SplitList<String> getHeaderList() {
        SplitList<String> headerList = new SplitList<String>(TAB);

        for (IMZTabColumn mzTabColumn : columnMapping.values()) {
            headerList.add(mzTabColumn.getHeader());
        }

        return headerList;
    }

    /**
     * Print a header line, each item split by 'TAB' character.
     * [PRH|PEH|PSH|SMH]    header1 header2 ...
     */
    @Override
    public String toString() {
        return section.getPrefix() + TAB + getHeaderList().toString();
    }

    /**
     * The offset record the position of MZTabColumn in header line. For example, protein header line,
     * the relationships between Logical Position, MZTabColumn, offset and order are like following structure:
     * Logical Position              MZTabColumn                       offset           order
     * "01"                          accession                         1                01
     * "02"                          description                       2                02
     * ......
     * "08"                          best_search_engine_score          8                08
     * "091"                         search_engine_score_ms_run[1]     9                09
     * "092"                         search_engine_score_ms_run[2]     10               09
     * "10"                          reliability                       11               10
     * "111"                         num_psms_ms_run[1]                12               11
     * "112"                         num_psms_ms_run[2]                13               11
     */
    public SortedMap<Integer, IMZTabColumn> getOffsetColumnsMap() {
        SortedMap<Integer, IMZTabColumn> map = new TreeMap<>();

        int offset = 1;
        for (IMZTabColumn column : columnMapping.values()) {
            map.put(offset++, column);
        }

        return map;
    }

    /**
     * Judge the column is optional or not, based one its header name.
     *
     * @param header SHOULD NOT be null!
     */
    public boolean isOptionalColumn(String header) {
        header = header.trim().toLowerCase();

        switch (section) {
//            case Protein_Header:
//                if (header.startsWith(ProteinColumn.SEARCH_ENGINE_SCORE.getName()) ||
//                        header.startsWith(ProteinColumn.NUM_PSMS.getName()) ||
//                        header.startsWith(ProteinColumn.NUM_PEPTIDES_DISTINCT.getName()) ||
//                        header.startsWith(ProteinColumn.NUM_PEPTIDES_UNIQUE.getName()) ||
//                        header.startsWith("protein_abundance_assay") ||
//                        header.startsWith("protein_abundance_study_variable") ||
//                        header.startsWith("protein_abundance_stdev_study_variable") ||
//                        header.startsWith("protein_abundance_std_error_study_variable")) {
//                    return true;
//                }
//                break;
//            case Peptide_Header:
//                if (header.startsWith(PeptideColumn.SEARCH_ENGINE_SCORE.getName()) ||
//                        header.startsWith("peptide_abundance_assay") ||
//                        header.startsWith("peptide_abundance_study_variable") ||
//                        header.startsWith("peptide_abundance_stdev_study_variable") ||
//                        header.startsWith("peptide_abundance_std_error_study_variable")) {
//                    return true;
//                }
//                break;
            case Small_Molecule_Header:
                //FIXME adapt to best id score
//                if (header.startsWith(SmallMoleculeColumn.SEARCH_ENGINE_SCORE.getName()) ||
//                        header.startsWith("smallmolecule_abundance_assay") ||
//                        header.startsWith("smallmolecule_abundance_study_variable") ||
//                        header.startsWith("smallmolecule_abundance_stdev_study_variable") ||
//                        header.startsWith("smallmolecule_abundance_std_error_study_variable")) {
//                    return true;
//                }
                break;
        }

        return header.startsWith("opt_");

    }

    /**
     * Query the MZTabColumn in factory, based on column header with case-insensitive.
     * Notice: for optional columns, header name maybe flexible. For example, num_psms_ms_run[1].
     * At this time, user SHOULD BE provide the full header name to query MZTabColumn.
     * If just provide num_psms_ms_run, return null.
     */
    public IMZTabColumn findColumnByHeader(String header) {
        header = header.trim();

        for (IMZTabColumn column : columnMapping.values()) {
            if (header.equalsIgnoreCase(column.getHeader())) {
                return column;
            }
        }

        return null;
    }

    /**
     * Query the MZTabColumn in factory, based on column logical position.
     */
    public IMZTabColumn findColumnByPosition(String logicalPosition) {
        return columnMapping.get(logicalPosition);
    }

    /**
     * Query all the MZTabColumn which have the same order in factory.
     */
    public SortedMap<String, IMZTabColumn> findAllColumnsByOrder(String order) {
        SortedMap<String, IMZTabColumn> mapping = new TreeMap<>();

        for (String logicalPosition : columnMapping.keySet()) {
            if (order.equals(getColumnOrder(logicalPosition))) {
                mapping.put(logicalPosition, columnMapping.get(logicalPosition));
            }
        }

        return mapping;
    }
}
