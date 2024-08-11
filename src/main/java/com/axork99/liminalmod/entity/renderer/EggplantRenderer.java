package com.axork99.liminalmod.entity.renderer;

import com.axork99.liminalmod.entity.projectile.thrown.EggplantEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class EggplantRenderer extends FlyingItemEntityRenderer<EggplantEntity> {

    public EggplantRenderer(EntityRendererFactory.Context context) {
        super(context, 2F, true);
    }

}
