package Criteria;


import Directory.FileSystemElement;

public class Criteria {

    public Criteria(String s){
        this.name = s;
    }
    boolean isSimple;
    private final String name;

    //Simple Criteria
    public enum AttrName {
        Name("name"), Type("type"), Size("size");
        private final String name;
        AttrName(String name){
            this.name = name;
        }
        public String toString() {
            return name;
        }
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
    public AttrName getAttrName() {
        return attrName;
    }
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
    public Criteria(String name, Criteria left, LogicOp logicOp, Criteria right){
        this.name = name;
        this.logicOp = logicOp;
        this.left = left;
        this.right = right;
        this.isSimple = false;
    }

    //Negation Criteria
    public Criteria(String name, LogicOp logicOp, Criteria left){
        this.name = name;
        this.logicOp = logicOp;
        this.left = left;
        this.isSimple = false;
    }

    public String getName(){
        return name;
    }
    public String getCriteria(){
        if(isSimple)
            return attrName + " " + op + " " + value;
        if(logicOp != LogicOp.NOT)
            return "(" + left.getCriteria() + ") " + logicOp + " (" + right.getCriteria() + ")";
        return left.getNegation();
    }


    private String getNegation() {
        if (isSimple) {
            return attrName + " " + Op.values()[op.ordinal() ^ 1] + " " + value;
        }
        if(logicOp != LogicOp.NOT)
            return "(" + left.getNegation() + ") " + LogicOp.values()[logicOp.ordinal() ^ 1] + " (" + right.getNegation() + ")";
        return left.getCriteria();
    }

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

    public static boolean checkName(String str) {
        return str.length() != 2 || !Character.isAlphabetic(str.charAt(0)) || !Character.isAlphabetic(str.charAt(1));
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Criteria){
            return name.equals(((Criteria)o).name);
        }
        return false;
    }
}
