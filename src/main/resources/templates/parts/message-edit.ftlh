<a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false"
   aria-controls="collapseExample">
    Message editor
</a>
<div class="collapse <#if message??>show</#if>" id="collapseExample">
    <div class="form-group mt-3">
        <form method="post" enctype="multipart/form-data">
            <div class="form-group">
                <input type="text" class="form-control ${(textError??)?string('is-invalid', '')}"
                       value="<#if message??>${message.text}</#if>" name="text" placeholder="Enter your message"/>
                <#if textError??>
                <div class="invalid-feedback">
                    ${textError}
                </div>
            </#if>
    </div>
    <div class="form-group">
        <input type="text" class="form-control ${(tagError??)?string('is-invalid', '')}" name="tag"
               value="<#if message??>${message.tag}</#if>"  placeholder="Enter tag"/>
        <#if tagError??>
        <div class="invalid-feedback">
            ${tagError}
        </div>
    </#if>
</div>
<div class="form-group">
    <div class="custom-file">
        <input type="file" name="file" id="customFile"/>
        <label class="custom-file-label" for="customFile">Choose file</label>
    </div>
</div>
<input type="hidden" name="_csrf" value="{_csrf.token}"/>

<div class="form-group">
    <button class="btn btn-primary"  type="submit">Save message</button>
</div>
</form>
</div>
</div>
