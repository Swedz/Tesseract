package net.swedz.tesseract.neoforge.compat.mi.network.packets;

import aztech.modern_industrialization.machines.gui.MachineMenuServer;
import aztech.modern_industrialization.network.MIStreamCodecs;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.swedz.tesseract.neoforge.compat.mi.guicomponent.configurationpanel.ConfigurationPanel;
import net.swedz.tesseract.neoforge.compat.mi.network.TesseractMIBasePacket;

/**
 * This was stolen from {@link aztech.modern_industrialization.network.machines.ChangeShapePacket} to make my own generic "configuration panel" component to be used for non-shape related configuring of machines.
 */
public record UpdateMachineConfigurationPanelPacket(
		int syncId, int line, boolean clickedLeftButton
) implements TesseractMIBasePacket
{
	public static final StreamCodec<ByteBuf, UpdateMachineConfigurationPanelPacket> STREAM_CODEC = StreamCodec.composite(
			MIStreamCodecs.BYTE,
			UpdateMachineConfigurationPanelPacket::syncId,
			ByteBufCodecs.VAR_INT,
			UpdateMachineConfigurationPanelPacket::line,
			ByteBufCodecs.BOOL,
			UpdateMachineConfigurationPanelPacket::clickedLeftButton,
			UpdateMachineConfigurationPanelPacket::new
	);
	
	@Override
	public void handle(Context ctx)
	{
		ctx.assertOnServer();
		
		AbstractContainerMenu menu = ctx.getPlayer().containerMenu;
		if(menu.containerId == syncId && menu instanceof MachineMenuServer machineMenu)
		{
			ConfigurationPanel.Server configurationPanel = machineMenu.blockEntity.getComponent(ConfigurationPanel.ID);
			configurationPanel.behavior.handleClick(line, clickedLeftButton ? -1 : +1);
		}
	}
}
