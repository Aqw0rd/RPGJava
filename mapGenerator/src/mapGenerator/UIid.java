package mapGenerator;

import java.util.EnumSet;

public enum UIid {
	ActionBar(),
	ActionSlots1(),
	ActionSlots2(),
	ActionSlots3(),
	ActionSlots4(),
	Inventory(),
	InvSlots1(),
	InvSlots2(),
	InvSlots3(),
	InvSlots4(),
	InvSlots5(),
	InvSlots6(),
	InvSlots7(),
	InvSlots8(),
	InvSlots9(),
	InGameMenu(),
	HealthBar(),
	ManaBar(),
	XpBar();
	
	public static final EnumSet<UIid> actionSlots = EnumSet.of(	ActionSlots1, 
																ActionSlots2,
																ActionSlots3,
																ActionSlots4);
	
	public static final EnumSet<UIid> invSlots = EnumSet.of(	InvSlots1, InvSlots2,
																InvSlots3, InvSlots4,
																InvSlots5, InvSlots6,
																InvSlots7, InvSlots8,
																InvSlots9);
}
