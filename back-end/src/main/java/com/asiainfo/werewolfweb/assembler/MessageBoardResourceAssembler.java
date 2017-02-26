package com.asiainfo.werewolfweb.assembler;

import com.asiainfo.werewolfweb.bean.MessageBoard;
import com.asiainfo.werewolfweb.resource.MessageBoardResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

/**
 * @author : huangchen
 * @description :
 * @date : 2016/12/6
 */
public class MessageBoardResourceAssembler extends ResourceAssemblerSupport<MessageBoard,
        MessageBoardResource> {
    /**
     * Creates a new {@link ResourceAssemblerSupport} using the given controller class and
     * resource type.
     *
     * @param controllerClass must not be {@literal null}.
     * @param resourceType    must not be {@literal null}.
     */
    public MessageBoardResourceAssembler(Class<?> controllerClass,
                                         Class<MessageBoardResource> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    public MessageBoardResource toResource(MessageBoard entity) {
        return new MessageBoardResource(entity);
    }
}
