package mapGenerator;

import java.util.EnumSet;

public enum UIid {
	ActionBar(),
	ActionSlots1(),
	ActionSlots2(),
	ActionSlots3(),
	ActionSlots4(),
	Inventory(),
	InGameMenu(),
	HealthBar(),
	ManaBar(),
	XpBar();
	
	public static final EnumSet<UIid> actionSlots = EnumSet.of(	ActionSlots1, 
																ActionSlots2,
																ActionSlots3,
																ActionSlots4);
}
