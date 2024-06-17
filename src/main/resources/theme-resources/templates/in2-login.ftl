<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
<#if section = "header">
${msg("registerTitle")}
<#elseif section = "form">
<form id="kc-register-form" class="${properties.kcFormClass!}" action="${url.registrationAction}" method="post">
            <input type="hidden" id="register" name="register"/>
            <input type="hidden" id="displayName" name="displayName" value="${(displayName!'')}"/>
            <input type="hidden" id="myid" name="myid" value="${(myid!'')}"/>
            <input type="hidden" id="cn" name="cn" value="${(cn!'')}"/>
            <input type="hidden" id="organizationIdentifier" name="organizationIdentifier" value="${(organizationIdentifier!'')}"/>
            <input type="hidden" id="organization" name="organization" value="${(organization!'')}"/>
            <input type="hidden" id="country" name="country" value="${(country!'')}"/>
            <input type="hidden" id="role" name="role" value="${(role!'')}"/>
            <input type="hidden" id="serialNumber" name="serialNumber" value="${(serialNumber!'')}"/>

            <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('name',properties.kcFormGroupErrorClass!)}">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="firstName" class="${properties.kcLabelClass!}">${msg("firstName")} ${name}</label>
                </div>
                <div class="${properties.kcInputWrapperClass!}">
                    <input type="text" id="firstName" class="${properties.kcInputClass!}" name="firstName" value="${(name!'')}" />
                </div>
            </div>

            <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('lastName',properties.kcFormGroupErrorClass!)}">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="lastName" class="${properties.kcLabelClass!}">${msg("lastName")}</label>
                </div>
                <div class="${properties.kcInputWrapperClass!}">
                    <input type="text" id="lastName" class="${properties.kcInputClass!}" name="lastName" value="${(lastName!'')}" />
                </div>
            </div>

            <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('email',properties.kcFormGroupErrorClass!)}">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="email" class="${properties.kcLabelClass!}">${msg("email")}</label>
                </div>
                <div class="${properties.kcInputWrapperClass!}">
                    <input type="text" id="email" class="${properties.kcInputClass!}" name="email" value="" autocomplete="email" />
                </div>
            </div>

            <#if recaptchaRequired??>
            <div class="form-group">
                <div class="${properties.kcInputWrapperClass!}">
                    <div class="g-recaptcha" data-size="compact" data-sitekey="${recaptchaSiteKey}"></div>
                </div>
            </div>
            </#if>

            <div class="${properties.kcFormGroupClass!}">
                <div id="kc-form-options" class="${properties.kcFormOptionsClass!}">
                    <div class="${properties.kcFormOptionsWrapperClass!}">
                        <span><a href="${url.loginUrl}">${kcSanitize(msg("backToLogin"))?no_esc}</a></span>
                    </div>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="submit" value="${msg("doRegister")}"/>
                </div>
            </div>
        </form>
    </#if>
</@layout.registrationLayout>