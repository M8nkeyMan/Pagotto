package com.pag.socialz.Enums;

public enum ItemType {
    LOAD(10),ITEM(11);

    private final int typeCode;

    ItemType(int typeCode){
        this.typeCode = typeCode;
    }

    public int getTypeCode(){
        return typeCode;
    }
}
