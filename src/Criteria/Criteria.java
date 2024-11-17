package Criteria;


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
            return attrName + " " + op.ordinal() + " " + value;
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

    public boolean check(CriteriaValue key){
        if(isSimple){
            switch (attrName) {
                case Name:
                    return key.contains(value.toString());
                case Type:
                    if(op == Op.NotEqual) return !key.equals(value);
                    return key.equals(value);
                case Size:
                    switch(op){
                        case Equal:
                            return key.getValue() == value.getValue();
                        case NotEqual:
                            return key.getValue() != value.getValue();
                        case Greater:
                            return key.getValue() > value.getValue();
                        case LessEqual:
                            return key.getValue() <= value.getValue();
                        case Less:
                            return key.getValue() < value.getValue();
                        case GreaterEqual:
                            return key.getValue() >= value.getValue();
                    }
            }
        }
        if(logicOp == LogicOp.AND)
            return left.check(key) && right.check(key);
        if(logicOp == LogicOp.OR)
            return left.check(key) || right.check(key);
        return !left.check(key);
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
