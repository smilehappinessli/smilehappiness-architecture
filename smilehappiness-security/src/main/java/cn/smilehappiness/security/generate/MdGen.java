package cn.smilehappiness.security.generate;

import cn.smilehappiness.security.dto.BaseRequest;
import cn.smilehappiness.security.dto.UserTokenInfo;
import cn.smilehappiness.security.utils.crypt.MdUtil;

/**
 * <p>
 * md gen
 * <p/>
 *
 * @author
 * @Date 2023/3/9 11:06
 */
public class MdGen {

    public static void main(String[] args) throws Exception {
        try {
            BaseRequest<UserTokenInfo> baseRequest = new BaseRequest<>();
            baseRequest.setBizContent(new UserTokenInfo());
            String md = MdUtil.genRequestFields(baseRequest);
            System.out.println(md);
        } catch (Exception e) {
            throw new Exception("failed to generate MD", e);
        }
    }
}
