/*
 * This file is part of the L2J Mobius project.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.lineage.gameserver.mods.mastery;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.lineage.Config;
import org.lineage.gameserver.data.xml.impl.SkillData;
import org.lineage.gameserver.data.xml.impl.SkillTreeData;
import org.lineage.gameserver.model.ExtractableSkill;
import org.lineage.gameserver.model.StatSet;
import org.lineage.gameserver.model.conditions.Condition;
import org.lineage.gameserver.model.effects.AbstractEffect;
import org.lineage.gameserver.model.skills.*;
import org.lineage.gameserver.model.skills.targets.TargetType;
import org.lineage.gameserver.model.stats.BaseStat;
import org.lineage.gameserver.model.stats.TraitType;
import org.lineage.gameserver.model.stats.functions.FuncTemplate;
import org.lineage.gameserver.network.serverpackets.FlyToLocation.FlyType;

import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class MasterySkill {
	private int _id;
	private int _level;
	private int _displayId;
	private int _displayLevel;
	private String _name;
	private SkillOperateType _operateType;
	private int _magic;
	private TraitType _traitType;
	private boolean _staticReuse;
	private int _mpConsume;
	private int _mpInitialConsume;
	private int _mpPerChanneling;
	private int _hpConsume;
	private int _itemConsumeCount;
	private int _itemConsumeId;
	private int _castRange;
	private int _effectRange;
	private boolean _isAbnormalInstant;
	private int _abnormalLevel;
	private AbnormalType _abnormalType;
	private int _abnormalTime;
	private AbnormalVisualEffect[] _abnormalVisualEffects = null;
	private AbnormalVisualEffect[] _abnormalVisualEffectsSpecial = null;
	private AbnormalVisualEffect[] _abnormalVisualEffectsEvent = null;
	private boolean _stayAfterDeath;
	private boolean _stayOnSubclassChange;
	private boolean _isRecoveryHerb;
	private int _hitTime;
	private int _coolTime;
	private int _reuseHashCode;
	private int _reuseDelay;
	private TargetType _targetType;
	private int _feed;
	private double _power;
	private double _pvpPower;
	private double _pvePower;
	private int _magicLevel;
	private int _lvlBonusRate;
	private int _activateRate;
	private int _minChance;
	private int _maxChance;
	private int _blowChance;
	private int _affectRange;
	private int[] _affectLimit = new int[2];
	private boolean _nextActionIsAttack;
	private boolean _removedOnAnyActionExceptMove;
	private boolean _removedOnDamage;
	private boolean _blockedInOlympiad;
	private byte _element;
	private int _elementPower;
	private BaseStat _basicProperty;
	private boolean _overhit;
	private int _minPledgeClass;
	private int _chargeConsume;
	private int _soulMaxConsume;
	private boolean _isHeroSkill; // If true the skill is a Hero Skill
	private boolean _isGMSkill; // True if skill is GM skill
	private boolean _isSevenSigns;
	private int _baseCritRate; // percent of success for skill critical hit (especially for PhysicalAttack & Blow - they're not affected by rCrit values or buffs).
	private boolean _directHpDmg; // If true then damage is being make directly
	private boolean _isTriggeredSkill; // If true the skill will take activation buff slot instead of a normal buff slot
	private int _effectPoint;
	private List<Condition> _preCondition;
	private List<Condition> _itemPreCondition;
	private List<FuncTemplate> _funcTemplates;
	private Map<EffectScope, List<AbstractEffect>> _effectLists = new EnumMap<>(EffectScope.class);
	private FlyType _flyType;
	private int _flyRadius;
	private float _flyCourse;
	private boolean _isDebuff;
	private String _attribute;
	private boolean _ignoreShield;
	private boolean _isSuicideAttack;
	private boolean _canBeDispeled;
	private boolean _isClanSkill;
	private boolean _excludedFromCheck;
	private boolean _simultaneousCast;
	private ExtractableSkill _extractableItems = null;
	private String _icon;
	private volatile Byte[] _effectTypes;
	private int _channelingSkillId;
	private int _channelingTickInitialDelay;
	private int _channelingTickInterval;
	private boolean _isPvPOnly;
	private boolean isProjectile;

	public MasterySkill(Skill skill) {
		for (Field field : skill.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				Field masteryField = getClass().getDeclaredField(field.getName());
				masteryField.setAccessible(true);
				masteryField.set(this, field.get(this));
			} catch (Exception e) {

			}
		}
	}

	public MasterySkill(MasterySkill skill) {

	}

	public Skill createNewMasterySkill() {
		StatSet set = new StatSet();
		_id = set.getInt("skill_id");
		_level = set.getInt("level");
		_displayId = set.getInt("displayId", _id);
		_displayLevel = set.getInt("displayLevel", _level);
		_name = set.getString("name", "");
		_operateType = set.getEnum("operateType", SkillOperateType.class);
		_magic = set.getInt("isMagic", 0);
		_traitType = set.getEnum("trait", TraitType.class, TraitType.NONE);
		_staticReuse = set.getBoolean("staticReuse", false);
		_mpConsume = set.getInt("mpConsume", 0);
		_mpInitialConsume = set.getInt("mpInitialConsume", 0);
		_mpPerChanneling = set.getInt("mpPerChanneling", _mpConsume);
		_hpConsume = set.getInt("hpConsume", 0);
		_itemConsumeCount = set.getInt("itemConsumeCount", 0);
		_itemConsumeId = set.getInt("itemConsumeId", 0);
		_castRange = set.getInt("castRange", -1);
		_effectRange = set.getInt("effectRange", -1);
		_abnormalLevel = set.getInt("abnormalLevel", 0);
		_abnormalType = set.getEnum("abnormalType", AbnormalType.class, AbnormalType.NONE);
		int abnormalTime = set.getInt("abnormalTime", 0);
		if (Config.ENABLE_MODIFY_SKILL_DURATION && Config.SKILL_DURATION_LIST.containsKey(_id)) {
			if ((_level < 100) || (_level > 140)) {
				abnormalTime = Config.SKILL_DURATION_LIST.get(_id);
			} else if ((_level >= 100) && (_level < 140)) {
				abnormalTime += Config.SKILL_DURATION_LIST.get(_id);
			}
		}

		_abnormalTime = abnormalTime;
		_isAbnormalInstant = set.getBoolean("abnormalInstant", false);

		AbnormalVisualEffect[][] abnormalVisualEffects = Skill.parseAbnormalVisualEffect(set.getString("abnormalVisualEffect", null));
		_abnormalVisualEffectsEvent = abnormalVisualEffects[0];
		_abnormalVisualEffectsSpecial = abnormalVisualEffects[1];
		_abnormalVisualEffects = abnormalVisualEffects[2];

		_attribute = set.getString("attribute", "");
		_stayAfterDeath = set.getBoolean("stayAfterDeath", false);
		_stayOnSubclassChange = set.getBoolean("stayOnSubclassChange", true);
		_hitTime = set.getInt("hitTime", 0);
		_coolTime = set.getInt("coolTime", 0);
		_isDebuff = set.getBoolean("isDebuff", false);
		_isRecoveryHerb = set.getBoolean("isRecoveryHerb", false);
		_feed = set.getInt("feed", 0);
		_reuseHashCode = SkillData.getSkillHashCode(_id, _level);
		if (Config.ENABLE_MODIFY_SKILL_REUSE && Config.SKILL_REUSE_LIST.containsKey(_id)) {
			_reuseDelay = Config.SKILL_REUSE_LIST.get(_id);
		} else {
			_reuseDelay = set.getInt("reuseDelay", 0);
		}

		_affectRange = set.getInt("affectRange", 0);

		final String affectLimit = set.getString("affectLimit", null);
		if (affectLimit != null) {
			try {
				final String[] valuesSplit = affectLimit.split("-");
				_affectLimit[0] = Integer.parseInt(valuesSplit[0]);
				_affectLimit[1] = Integer.parseInt(valuesSplit[1]);
			} catch (Exception e) {
				throw new IllegalArgumentException("SkillId: " + _id + " invalid affectLimit value: " + affectLimit + ", \"percent-percent\" required");
			}
		}

		_targetType = set.getEnum("targetType", TargetType.class, TargetType.SELF);
		_power = set.getFloat("power", 0.f);
		_pvpPower = set.getFloat("pvpPower", (float) _power);
		_pvePower = set.getFloat("pvePower", (float) _power);
		_magicLevel = set.getInt("magicLevel", 0);
		_lvlBonusRate = set.getInt("lvlBonusRate", 0);
		_activateRate = set.getInt("activateRate", -1);
		_minChance = set.getInt("minChance", Config.MIN_ABNORMAL_STATE_SUCCESS_RATE);
		_maxChance = set.getInt("maxChance", Config.MAX_ABNORMAL_STATE_SUCCESS_RATE);
		_ignoreShield = set.getBoolean("ignoreShld", false);
		_nextActionIsAttack = set.getBoolean("nextActionAttack", false);
		_removedOnAnyActionExceptMove = set.getBoolean("removedOnAnyActionExceptMove", false);
		_removedOnDamage = set.getBoolean("removedOnDamage", false);
		_blockedInOlympiad = set.getBoolean("blockedInOlympiad", false);
		_element = set.getByte("element", (byte) -1);
		_elementPower = set.getInt("elementPower", 0);
		_basicProperty = set.getEnum("basicProperty", BaseStat.class, BaseStat.NONE);
		_overhit = set.getBoolean("overHit", false);
		_isSuicideAttack = set.getBoolean("isSuicideAttack", false);
		_minPledgeClass = set.getInt("minPledgeClass", 0);
		_chargeConsume = set.getInt("chargeConsume", 0);
		_soulMaxConsume = set.getInt("soulMaxConsumeCount", 0);
		_blowChance = set.getInt("blowChance", 0);
		_isHeroSkill = SkillTreeData.getInstance().isHeroSkill(_id, _level);
		_isGMSkill = SkillTreeData.getInstance().isGMSkill(_id, _level);
		_isSevenSigns = (_id > 4360) && (_id < 4367);
		_isClanSkill = SkillTreeData.getInstance().isClanSkill(_id, _level);
		_baseCritRate = set.getInt("baseCritRate", 0);
		_directHpDmg = set.getBoolean("dmgDirectlyToHp", false);
		_isTriggeredSkill = set.getBoolean("isTriggeredSkill", false);
		_effectPoint = set.getInt("effectPoint", 0);
		_flyType = set.getEnum("flyType", FlyType.class, null);
		_flyRadius = set.getInt("flyRadius", 0);
		_flyCourse = set.getFloat("flyCourse", 0);
		_canBeDispeled = set.getBoolean("canBeDispeled", true);
		_excludedFromCheck = set.getBoolean("excludedFromCheck", false);
		_simultaneousCast = set.getBoolean("simultaneousCast", false);

		final String capsuled_items = set.getString("capsuled_items_skill", null);
		if (capsuled_items != null) {
			if (capsuled_items.isEmpty()) {
				log.warn("Empty Extractable Item Skill data in Skill Id: {}", _id);
			}

			_extractableItems = Skill.parseExtractableSkill(_id, _level, capsuled_items);
		}

		_icon = set.getString("icon", "icon.skill0000");
		_channelingSkillId = set.getInt("channelingSkillId", 0);
		_channelingTickInterval = set.getInt("channelingTickInterval", 2) * 1000;
		_channelingTickInitialDelay = set.getInt("channelingTickInitialDelay", _channelingTickInterval / 1000) * 1000;
		_isPvPOnly = set.getBoolean("isPvPOnly", false);
		isProjectile = set.getBoolean("isProjectile", false);

		return new Skill(set);
	}

	@Override
	public String toString() {
		return "Mastery " + _name + "(" + _id + "," + _level + ")";
	}
}