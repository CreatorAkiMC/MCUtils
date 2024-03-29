package com.aki.mcutils.APICore.Utils.block.property.unlisted;

import com.google.common.base.MoreObjects;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by covers1624 on 30/10/2016.
 */
public class UnlistedFluidStackProperty extends UnlistedPropertyBase<FluidStack> {

    public UnlistedFluidStackProperty(String name) {
        super(name);
    }

    @Override
    public Class<FluidStack> getType() {
        return FluidStack.class;
    }

    @Override
    public String valueToString(FluidStack value) {
        return MoreObjects.toStringHelper("FluidStack").add("Fluid", value.getFluid().getName()).add("Amount", value.amount).add("Tag", value.tag.toString()).toString();
    }
}
