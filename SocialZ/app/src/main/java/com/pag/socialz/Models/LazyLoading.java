package com.pag.socialz.Models;

import com.pag.socialz.Enums.ItemType;

public interface LazyLoading {
    ItemType getItemType();
    void setItemType(ItemType itemType);
}
