package com.aki.mcutils.APICore.Utils.block.property.unlisted;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

/**
 * Created by covers1624 on 21/02/2017.
 */
public class UnlistedIntArrayProperty extends UnlistedPropertyBase<int[]> {

    public UnlistedIntArrayProperty(String name) {
        super(name);
    }

    @Override
    public Class<int[]> getType() {
        return int[].class;
    }

    @Override
    public String valueToString(int[] value) {
        ToStringHelper helper = MoreObjects.toStringHelper("IntArray");
        if (value != null) {
            for (int i1 = 0; i1 < value.length; i1++) {
                int i = value[i1];
                helper.add("Index:" + i1, i);
            }
        }
        return helper.toString();
    }
}
