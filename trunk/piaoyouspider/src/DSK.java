import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

import com.bst.flex.Area;
import com.bst.flex.PriceLevel;
import com.bst.flex.SeatIcon;

import flex.messaging.messages.AcknowledgeMessageExt;

/**
 * 国家大剧院AMF请求返回的对象,用于序列化和反序列化
 * @author Administrator
 * @see {@link Area} , {@link PriceLevel} , {@link SeatIcon}
 *
 */

public class DSK extends AcknowledgeMessageExt implements Serializable,Externalizable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -991202033252074967L;
	
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		super.readExternal(in);
	}
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
	}
}
