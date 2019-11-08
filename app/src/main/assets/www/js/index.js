/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var callbacks = [];
var baseId = 1;
var app = {
    // Application Constructor
    initialize: function() {
        console.log('app initialize');
        // this.initScript("http://192.168.199.139:9020/long-form-init.js");
       this.initScript("http://192.168.199.88:8023/long-form-init.js");
       this.initForms();
    },

    initScript: function (url) {
        var initScript = document.createElement("script");
        initScript.type = "text/javascript";
        initScript.src = url+"?randomNum="+Math.random();
        document.head.appendChild(initScript);
        console.log(document.head);
    },

    initForms: function () {
        const request = new XMLHttpRequest();

        request.onreadystatechange = function () {
          if (request.readyState == 4) {
              if (request.status == 200) {
                  var resp = request.response;
                  console.log("response:" + resp);
                  app.insertForms(JSON.parse(resp));
              } else {
                  console.log("http status error![" + request.status + "]" + request.statusText + "," + request.response);
              }
          }else {
              console.log("http state error![" + request.readyState + "]");
          }
        };

        var url = "http://192.168.199.139:9020/lfbe/engine/core/form/search";
        var data = {
            app_key: "42b99285d25a4dc9bd0d28306f67b406",
            platforms: ["Android"],
            roles: ["admin"],
        };
        request.open("POST", url, true);
        request.setRequestHeader("Content-type","application/json");
        request.send(JSON.stringify(data));
    },

    insertForms: function (formDatas) {
        var forms = document.getElementById("forms");
        var ul = document.createElement("ul");
        for(i = 0; i < formDatas.length; i ++) {
            var li = document.createElement("li");
            var textNode = document.createTextNode(formDatas[i].name);
            li.appendChild(textNode);
            ul.appendChild(li);
            li.setAttribute("onclick", "renderPage('42b99285d25a4dc9bd0d28306f67b406','"+formDatas[i].id+"', 'admin','dce422af-8530-421d-bbc5-bc1e1a0f7a9c', {'username':'longform'})")
        }
        forms.appendChild(ul);
    },

    callback: function(json) {
        console.log("callback..." + json);
    },

    callAndroid: function () {
        console.log("click");
         var params = {
             id: "10",
             name: "arnold"
         };
        // var result = scanModule.scan(params);
        // console.log("result:" + result.barcode);

        var success = function(message){
            console.log("success = "+message);
        };
        var fail = function(message){
            console.log("fail = "+message);
        };

//        this.nativeExec2(success, fail, "scanModule", "scan", params);
        this.nativeExec3(success, fail, "/scanModule/aa/scan", params);
    },

    nativeExec: function (success, fail, module, method, params) {
        var callbackId = module+baseId++;
        console.log(callbackId);
        if (success || fail) {
            callbacks[callbackId] = {success:success, fail:fail};
        }
        var cmd = "android.exec(" + "'"+module+"'" + "," + "'"+method+"'" + "," + "'"+callbackId+"'" + "," + "'"+JSON.stringify(params)+"'" + ")";
        console.log('NATIVE INVOLK: ' + cmd);
        eval(cmd);
    },

    nativeExec2: function (success, fail, module, method, params) {
        var callbackId = module+baseId++;
        var callbackName = "nativeCallback";
        console.log(callbackId);
        if (success || fail) {
            callbacks[callbackId] = {success:success, fail:fail};
        }
        var cmd = "android.exec(" + "'"+module+"'" + "," + "'"+method+"'" + "," + "'"+callbackName+"'" + "," + "'"+callbackId+"'" + "," + "'"+JSON.stringify(params)+"'" + ")";
        console.log('NATIVE INVOLK: ' + cmd);
        eval(cmd);
    },

    nativeExec3: function (success, fail, path, params) {
        var callbackId = module+baseId++;
        var callbackName = "nativeCallback";
        console.log(callbackId);
        if (success || fail) {
            callbacks[callbackId] = {success:success, fail:fail};
        }
        var cmd = "android.exec(" + "'"+path+"'" + "," + "'"+callbackName+"'" + "," + "'"+callbackId+"'" + "," + "'"+JSON.stringify(params)+"'" + ")";
        console.log('NATIVE INVOLK: ' + cmd);
        eval(cmd);
    },

    nativeCallback: function(callbackId, success, result) {
        console.log("nativeCallback:" + callbackId + ", " + success + ", " + result);
        var callback = callbacks[callbackId];
        console.log(callback);
        if (success) {
//            callback.success && callback.success.apply(result);
            callback.success(result);
        } else {
//            callback.fail && callback.fail.apply(result);
            callback.fail(result);
        }
    },

    renderWebPage: function (appKey, pageId, role, token, options) {
        console.log("renderWebPage");
        renderPage(appKey, pageId, role, token, options);
    },

    callJs: function () {
        console.log("callJs");
    },
};

app.initialize();