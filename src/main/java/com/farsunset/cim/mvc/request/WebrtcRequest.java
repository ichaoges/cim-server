package com.farsunset.cim.mvc.request;

import com.farsunset.cim.annotation.CreateAction;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel("单人通话ice、offer、answer同步请求体")
@Data
public class WebrtcRequest implements Serializable {

    @NotNull(message = "UID不能为空", groups = CreateAction.class)
    @ApiModelProperty("对方UID")
    private String uid;

    @NotEmpty(message = "content不能超过2000个字符", groups = CreateAction.class)
    @ApiModelProperty("ice信息json、offer或者answer的sdp")
    private String content;
}
