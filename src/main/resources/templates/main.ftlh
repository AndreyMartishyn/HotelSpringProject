<#import "parts/common.ftlh" as c>
<@c.page>

<div class="form-row">
    <div class="form-group col-md-6">
        <form method="get" action="/main" class="form-inline">
            <input type="text" name="filter" class="form-control" value="${filter?ifExists}" placeholder="Enter tag">
            <button class="btn btn-primary ml-2" type="submit">Find</button>
        </form>
    </div>
</div>

<#include "parts/message-edit.ftlh" />
<#include "parts/message-list.ftlh" />
</@c.page>