<#macro login path isRegisterForm>
<form action="${path}" method="post">
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">User name</label>
        <div class="col-sm-4">
            <input type="text" name="username" class="form-control ${(usernameError??)?string('is-invalid', '')}"
                   value="<#if user??>${user.username}</#if>" placeholder="Enter your username"/>
            <#if usernameError??>
            <div class="invalid-feedback">
                ${usernameError}
            </div>

        </#if>
    </div>
    </div>

    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Password</label>
        <div class="col-sm-4">
            <input type="password" name="password" class="form-control ${(passwordError??)?string('is-invalid', '')}"
                   placeholder="Enter your password"/>
            <#if passwordError??>
            <div class="invalid-feedback">
                ${passwordError}
            </div>
        </#if>
    </div>
    </div>

    <#if isRegisterForm>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Repeat password</label>
        <div class="col-sm-4">
            <input type="password" name="confirmPassword"
                   class="form-control ${(confirmPasswordError??)?string('is-invalid', '')}"
                   placeholder="Repeat your password"/>
            <#if confirmPasswordError??>
            <div class="invalid-feedback">
                ${confirmPasswordError}
            </div>
        </#if>
    </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Your email</label>
        <div class="col-sm-4">
            <input type="email" name="email" class="form-control ${(emailError??)?string('is-invalid', '')}"
                   value="<#if user??>${user.email}</#if>" placeholder="some@email.com"/>
            <#if emailError??>
            <div class="invalid-feedback">
                ${emailError}
            </div>
        </#if>
    </div>
    </div>
    <div class="col-sm-6">
    <div class="g-recaptcha" data-sitekey="6LdVlBIeAAAAAKBiWF_Ian8Or9q7KlXE2c7gzAbN"></div>
    <#if captchaError??>
    <div class="alert alert-danger" role="alert">
        ${captchaError}
    </div>
    </#if>
    </div>
</#if>
<input type="hidden" name="_csrf" value="{_csrf.token}"/>
<#if !isRegisterForm>
<a href="/registration">Add new user</a>
</#if>
<button class="btn btn-primary" type="submit"/><#if isRegisterForm>Create<#else>Sign in</#if></button>
</form>
</#macro>

<#macro logout>
<form action="/logout" method="post">
    <input type="hidden" name="_csrf" value="{_csrf.token}"/>
    <button class="btn btn-primary" type="submit">Sign out</button>
</form>
</#macro>