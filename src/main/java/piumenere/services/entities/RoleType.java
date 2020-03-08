package piumenere.services.entities;

public enum RoleType {
    
    DEFAULT,
    ADMINISTRATOR,
    SUPERADMINISTRATOR;
    
    private RoleType() {
    }
    
    public static RoleType fromString(String stringValue){
    	for (RoleType value : values()){
    		if (value.toString().equals(stringValue)) return value;
    	}
    	return DEFAULT;
    }
    
}