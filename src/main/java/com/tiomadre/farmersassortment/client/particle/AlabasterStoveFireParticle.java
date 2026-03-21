package com.tiomadre.farmersassortment.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class AlabasterStoveFireParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected AlabasterStoveFireParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd);
        this.sprites = sprites;
        this.friction = 0.96F;
        this.gravity = -0.02F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.xd = xd;
        this.yd = yd + level.random.nextFloat() * 0.02F;
        this.zd = zd;
        this.quadSize *= 0.75F + level.random.nextFloat() * 0.35F;
        this.lifetime = 12 + level.random.nextInt(6);
        this.rCol = 1.0F;
        this.gCol = 0.96F;
        this.bCol = 0.82F;
        this.alpha = 0.95F;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        this.yd += 0.002D;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= this.friction;
        this.yd *= this.friction;
        this.zd *= this.friction;
        if (this.onGround) {
            this.xd *= 0.7D;
            this.zd *= 0.7D;
        }

        this.alpha = 1.0F - ((float) this.age / (float) this.lifetime);
        this.quadSize = this.quadSize * 0.97F;
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public int getLightColor(float partialTick) {
        return 240;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        float life = ((float) this.age + scaleFactor) / (float) this.lifetime;
        return this.quadSize * Mth.clamp(1.0F - life * life * 0.5F, 0.0F, 1.0F);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new AlabasterStoveFireParticle(level, x, y, z, xd, yd, zd, this.sprites);
        }
    }
}