<#import "parts/common.ftlh" as c>
<@c.page>


<#if isCurrentUser>
    <#include "parts/message-edit.ftlh" />
</#if>

    <#include "parts/message-list.ftlh" />

</@c.page>