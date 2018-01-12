package com.gmail.berndivader.mythicmobsext;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.utils.Utils;
import com.gmail.berndivader.utils.Vec2D;
import com.gmail.berndivader.volatilecode.VolatileHandler;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class PlayerGoggleMechanic 
extends 
SkillMechanic 
implements
ITargetedEntitySkill {
	public static String str="mmGoggle";
	private Long dur;
	private VolatileHandler vh=Main.getPlugin().getVolatileHandler();

	public PlayerGoggleMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.dur=(Long)mlc.getInteger(new String[] { "duration", "d" }, 120);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!target.isPlayer()||target.getBukkitEntity().hasMetadata(str)) target.getBukkitEntity().removeMetadata(str,Main.getPlugin());
		target.getBukkitEntity().setMetadata(str, new FixedMetadataValue(Main.getPlugin(), true));
		final Player p=(Player)target.getBukkitEntity();
		final AbstractEntity caster=data.getCaster().getEntity();
		final Long d=this.dur;
		new BukkitRunnable() {
			Long count=0;
			@Override
			public void run() {
				if (p==null || p.isDead() || count>d || !p.hasMetadata(str)) {
					p.removeMetadata(str, Main.getPlugin());
					this.cancel();
				} else {
					Vec2D v=Utils.lookAtVec(p.getEyeLocation(),
							caster.getBukkitEntity().getLocation().add(0,caster.getEyeHeight(), 0));
					PlayerGoggleMechanic.this.vh.playerConnectionLookAt(p,(float)v.getX(),(float)v.getY());
				}
				count++;
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(), 1L,1L);
		return true;
	}

}
