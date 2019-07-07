package yoncey.gdkm.weixin.json;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonRedisSerializer extends Jackson2JsonRedisSerializer<Object> {

	private ObjectMapper objectMapper = new ObjectMapper();

	public JsonRedisSerializer() {
		super(Object.class);
	}

	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null) {
			return null;
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		DataInputStream in = new DataInputStream(bis);
		try {
			int len = in.readInt();
			byte[] classNameBytes = new byte[len];
			in.readFully(classNameBytes);
			String className = new String(classNameBytes, "UTF-8");
			@SuppressWarnings("unchecked")
			Class<Object> cla = (Class<Object>) Class.forName(className);
			Object o = objectMapper.readValue(Arrays.copyOfRange(bytes, len + 4, bytes.length), cla);
			return o;

		} catch (IOException | ClassNotFoundException e) {
			throw new SerializationException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public byte[] serialize(Object t) throws SerializationException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bos);
		try {
			String className = t.getClass().getName();
			byte[] classNameBytes = className.getBytes();

			out.writeInt(classNameBytes.length);
			out.write(classNameBytes);
			out.write(super.serialize(t));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
}

