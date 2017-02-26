package com.asiainfo.werewolfweb.assembler;

import com.asiainfo.werewolfweb.bean.TopOneBoard;
import com.asiainfo.werewolfweb.resource.TopOneBoardResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

/**
 * @author : huangchen
 * @description :
 * @date : 2016/12/6
 */
public class TopOneBoardResourceAssembler extends ResourceAssemblerSupport<TopOneBoard,
        TopOneBoardResource> {
    /**
     * Creates a new {@link ResourceAssemblerSupport} using the given controller class and
     * resource type.
     *
     * @param controllerClass must not be {@literal null}.
     * @param resourceType    must not be {@literal null}.
     */
    public TopOneBoardResourceAssembler(Class<?> controllerClass,
                                        Class<TopOneBoardResource> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    public TopOneBoardResource toResource(TopOneBoard entity) {
        return new TopOneBoardResource(entity);
    }
}
