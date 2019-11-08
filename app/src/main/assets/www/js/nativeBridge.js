nativeExec: function (success, fail, module, method, params) {
        var callbackId;
        if (success || fail) {
            this.callbacks[callbackId] = {success:success, fail:fail};
        }
        var cmd = module + '.' + method + '(' + JSON.stringify(params) + callbackId')';
        eval(cmd);
    },

    nativeCallback: function(callbackId, success, result) {
        var callback = this.callbacks[callbackId];
        if(success) {
            callback.sucess(result);
        }else {
            callback.fail(result);
        }
    },