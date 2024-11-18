package Criteria;


import Directory.FileSystemElement;

/**
 *  Define the three types of Criteria
 */
public class Criteria {

    /**
     * @param s CriteraName
     */
    public Criteria(String s){
        this.name = s;
    }
    private boolean isSimple;
    private final String name;

    /**
     * Criteria Attribute Name
     */
    //Simple Criteria
    public enum AttrName {
        /**
         * Check if name contains certain String
         */
        Name("name"),
        /**
         * Check if type euquals certain String
         */
        Type("type"),
        /**
         * Check size relationship
         */
        Size("size");
        private final String name;
        AttrName(String name){
            this.name = name;
        }
        public String toString() {
            return name;
        }

        /**
         * @param str The String needed to convert.
         * @return The corresponding Attribute.
         */
        public static AttrName fromString(String str) {
            for(AttrName attrName : AttrName.values()){
                if(str.equals(attrName.toString())){
                    return attrName;
                }
            }
            return null;
        }
    }
    private AttrName attrName;
    private Op op;
    private CriteriaValue value;

    /**
     * @param name Criteria Name
     * @param attrName Attribute Type
     * @param op Op Type
     * @param value The value to compare with
     */
    public Criteria(String name, AttrName attrName, Op op, CriteriaValue value){
        this.name = name;
        this.attrName = attrName;
        this.op = op;
        this.value = value;
        this.isSimple = true;
    }

    //Composite Criteria
    private LogicOp logicOp;
    private Criteria left, right;

    /**
     * @param name Criteria Name
     * @param left The left Criteria in the Binary Criteria
     * @param logicOp The logic operator applied to the Binary Criteria
     * @param right The right Criteria in the Binary Criteria
     */
    public Criteria(String name, Criteria left, LogicOp logicOp, Criteria right){
        this.name = name;
        this.logicOp = logicOp;
        this.left = left;
        this.right = right;
        this.isSimple = false;
    }

    /**
     * @param name Criteria Name
     * @param logicOp Logic operation not
     * @param left The left Criteria in the Binary Criteria
     */
    //Negation Criteria
    public Criteria(String name, LogicOp logicOp, Criteria left){
        this.name = name;
        this.logicOp = logicOp;
        this.left = left;
        this.isSimple = false;
    }

    /**
     * @return Criteria Name
     */
    public String getName(){
        return name;
    }

    /**
     * @return The String Represent of Criteria
     */
    public String getCriteria(){
        if(isSimple)
            return attrName + " " + op + " " + value;
        if(logicOp != LogicOp.NOT)
            return "(" + left.getCriteria() + ") " + logicOp + " (" + right.getCriteria() + ")";
        return left.getNegation();
    }

    /**
     * @param simple If the Criteria is simple Criteria
     */
    public void setSimple(boolean simple) {
        isSimple = simple;
    }

    private String getNegation() {
        if (isSimple) {
            return attrName + " " + Op.values()[op.ordinal() ^ 1] + " " + value;
        }
        if(logicOp != LogicOp.NOT)
            return "(" + left.getNegation() + ") " + LogicOp.values()[logicOp.ordinal() ^ 1] + " (" + right.getNegation() + ")";
        return left.getCriteria();
    }

    /**
     * @param file The file to be checked
     * @return Whether the file fulfilled the Criteria
     */
    public boolean check(FileSystemElement file){
        if(isSimple){
            CriteriaStringValue sKey;
            CriteriaIntValue iKey = CriteriaIntValue.parse(file.getSize());
            switch (attrName) {
                case Name:
                    sKey = CriteriaStringValue.parse(file.getName());
                    return sKey.contains(value.toString());
                case Type:
                    sKey = CriteriaStringValue.parse(file.getType());
                    return sKey.equals(value);
                case Size:
                    switch(op){
                        case Equal:
                            return iKey.getValue() == value.getValue();
                        case NotEqual:
                            return iKey.getValue() != value.getValue();
                        case Greater:
                            return iKey.getValue() > value.getValue();
                        case LessEqual:
                            return iKey.getValue() <= value.getValue();
                        case Less:
                            return iKey.getValue() < value.getValue();
                        case GreaterEqual:
                            return iKey.getValue() >= value.getValue();
                    }
            }
        }
        if(logicOp == LogicOp.AND)
            return left.check(file) && right.check(file);
        if(logicOp == LogicOp.OR)
            return left.check(file) || right.check(file);
        return !left.check(file);
    }

    /**
     * @param str The CriteriaName to be checked
     * @return whether the CriteriaName fulfilled the requirement
     */
    public static boolean checkName(String str) {
        return str.length() != 2 || !Character.isAlphabetic(str.charAt(0)) || !Character.isAlphabetic(str.charAt(1));
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Criteria){
            return getName().equals(((Criteria) o).getName());
        }
        return false;
    }
}
