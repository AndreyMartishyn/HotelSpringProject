<#import "parts/common.ftlh" as c>

<@c.page>
<h5>Hello, ${username}. Here you can edit your credentials</h5>
${message?ifExists}
<form method="post">
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Password</label>
        <div class="col-sm-4">
            <input type="password" name="password" class="form-control" placeholder="Enter your password"/>
        </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Your email</label>
        <div class="col-sm-4">
            <input type="email" name="email" class="form-control" placeholder="some@email.com" value="${email!''}"/>
        </div>
    </div>
    <input type="hidden" name="_csrf" value="{_csrf.token}"/>
    <button class="btn btn-primary" type="submit">Save</button>
</form>
</@c.page>