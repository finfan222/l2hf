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
package org.lineage.loginserver.network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.lineage.commons.network.codecs.CryptCodec;
import org.lineage.commons.network.codecs.LengthFieldBasedFrameEncoder;
import org.lineage.commons.network.codecs.PacketDecoder;
import org.lineage.commons.network.codecs.PacketEncoder;
import org.lineage.commons.util.crypt.LoginCrypt;
import org.lineage.loginserver.LoginController;

import javax.crypto.SecretKey;
import java.nio.ByteOrder;

/**
 * @author Nos
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel>
{
	private static final LengthFieldBasedFrameEncoder LENGTH_ENCODER = new LengthFieldBasedFrameEncoder();
	private static final PacketEncoder PACKET_ENCODER = new PacketEncoder(0x8000 - 2);
	
	@Override
	protected void initChannel(SocketChannel ch)
	{
		final SecretKey newKey = LoginController.getInstance().generateBlowfishKey();
		final LoginClient client = new LoginClient(newKey);
		ch.pipeline().addLast(new BannedIpFilter());
		ch.pipeline().addLast("length-decoder", new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 0x8000 - 2, 0, 2, -2, 2, false));
		ch.pipeline().addLast("length-encoder", LENGTH_ENCODER);
		ch.pipeline().addLast("crypt-codec", new CryptCodec(new LoginCrypt(newKey)));
		// ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
		ch.pipeline().addLast("packet-decoder", new PacketDecoder<>(IncomingPackets.PACKET_ARRAY, client));
		ch.pipeline().addLast("packet-encoder", PACKET_ENCODER);
		ch.pipeline().addLast(client);
	}
}
