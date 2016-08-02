package org.singledog.monkey.comm;

import org.springframework.util.StringUtils;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

/**
 * Created by Adam.Wu on 2016/6/28.
 */
public class MachineIdentifier {

    public static String getMachineId() {
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            List<String> macs = new ArrayList<>();
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = enumeration.nextElement();
                if (!networkInterface.isLoopback() && !networkInterface.isVirtual() && !networkInterface.isPointToPoint()
                        && !StringUtils.isEmpty(networkInterface.getHardwareAddress())) {
                    byte[] macarray = networkInterface.getHardwareAddress();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (byte b : macarray) {
                        stringBuilder.append(Integer.toHexString(b));
                    }
                    if (!macs.contains(stringBuilder.toString()))
                        macs.add(stringBuilder.toString());
                }
            }

            if (macs.size() > 0) {
                Collections.sort(macs);
                return MD5.md5Encrypt(Arrays.toString(macs.toArray(new String[0])));
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return null;
    }
}
