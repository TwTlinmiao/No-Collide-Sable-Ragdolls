package dev.nocollideragdoll.mixin;

import dev.nocollideragdoll.NoCollideRagdoll;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMoveMixin {
    @Inject(method = "move", at = @At("HEAD"))
    private void ncrOnMoveStart(MoverType type, Vec3 pos, CallbackInfo ci) {
        NoCollideRagdoll.INSIDE_ENTITY_MOVE.set(true);
    }

    @Inject(method = "move", at = @At("RETURN"))
    private void ncrOnMoveEnd(MoverType type, Vec3 pos, CallbackInfo ci) {
        NoCollideRagdoll.INSIDE_ENTITY_MOVE.set(false);
    }
}