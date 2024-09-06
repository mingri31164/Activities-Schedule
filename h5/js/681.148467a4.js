"use strict";(self["webpackChunkactivity"]=self["webpackChunkactivity"]||[]).push([[681],{528:function(t,e,n){n.r(e),n.d(e,{default:function(){return et}});var o=function(){var t=this,e=t._self._c;t._self._setupProxy;return e("div",{directives:[{name:"show",rawName:"v-show",value:t.loadFlag,expression:"loadFlag"}],staticClass:"user-page"},[e("div",{staticClass:"page-cont"},[e("GoBack"),e("main",[e("div",{staticClass:"user-info"},[e("img",{attrs:{src:t.userInfo.pic,alt:""}}),e("h2",[t._v(t._s(t.userInfo.uname))]),e("p",[t._v(t._s(t.userInfo.phone))])]),e("ul",{staticClass:"ad-info"},[e("li",[e("img",{attrs:{src:n(8671),alt:""}}),e("div",{staticClass:"info-text"},[e("p",[t._v(t._s(t.userInfo.games||0))]),e("span",[t._v("参与活动")])])]),e("li",[e("img",{attrs:{src:n(6585),alt:""}}),e("div",{staticClass:"info-text"},[e("p",[t._v(t._s(t.userInfo.products||0))]),e("span",[t._v("中奖记录")])])])]),e("div",{staticClass:"my-prize"},[e("div",{staticClass:"prize-title"},[t._v("我的奖品")]),e("div",{staticClass:"prize-list-mask"},[t.prizeList&&t.prizeList.length>0?e("ul",{staticClass:"prize-list"},t._l(t.prizeList,(function(n){return e("li",{key:n.id},[e("span",[t._v(t._s(n.title||"-"))]),e("span",[t._v(t._s(n.name||"-"))]),e("span",[t._v(t._s(n.hittime?n.hittime.slice(0,11):"-"))])])})),0):e("div",{staticClass:"default-page"},[e("p",[t._v("很遗憾，您未中奖")])])])]),e("div",{staticClass:"exit-btn",on:{click:t.exitFun}},[t._v("退出")])])],1)])},i=[],s=n(6318),a=(n(560),n(2782)),l=n(7774),r=n(4582),c=n(7378),u=n(7195),d=n(2715),h=n.n(d),f=n(161),p=n(868),v=n(1597),g="van-hairline",m=g+"--top",y=g+"--left",C=g+"--surround",k=n(4674),B=n(8745),x=n(7392),S=n(6975),b=n(8372),w=(0,f.d)("button"),$=w[0],z=w[1];function O(t,e,n,o){var i,s=e.tag,a=e.icon,l=e.type,r=e.color,c=e.plain,u=e.disabled,d=e.loading,f=e.hairline,p=e.loadingText,v=e.iconPosition,g={};function m(t){e.loading&&t.preventDefault(),d||u||((0,B.j8)(o,"click",t),(0,x.fz)(o))}function y(t){(0,B.j8)(o,"touchstart",t)}r&&(g.color=c?r:"white",c||(g.background=r),-1!==r.indexOf("gradient")?g.border=0:g.borderColor=r);var k=[z([l,e.size,{plain:c,loading:d,disabled:u,hairline:f,block:e.block,round:e.round,square:e.square}]),(i={},i[C]=f,i)];function w(){return d?n.loading?n.loading():t(b.Z,{class:z("loading"),attrs:{size:e.loadingSize,type:e.loadingType,color:"currentColor"}}):n.icon?t("div",{class:z("icon")},[n.icon()]):a?t(S.Z,{attrs:{name:a,classPrefix:e.iconPrefix},class:z("icon")}):void 0}function $(){var o,i=[];return"left"===v&&i.push(w()),o=d?p:n.default?n.default():e.text,o&&i.push(t("span",{class:z("text")},[o])),"right"===v&&i.push(w()),i}return t(s,h()([{style:g,class:k,attrs:{type:e.nativeType,disabled:u},on:{click:m,touchstart:y}},(0,B.ED)(o)]),[t("div",{class:z("content")},[$()])])}O.props=(0,c.Z)({},x.g2,{text:String,icon:String,color:String,block:Boolean,plain:Boolean,round:Boolean,square:Boolean,loading:Boolean,hairline:Boolean,disabled:Boolean,iconPrefix:String,nativeType:String,loadingText:String,loadingType:String,tag:{type:String,default:"button"},type:{type:String,default:"default"},size:{type:String,default:"normal"},loadingSize:{type:String,default:"20px"},iconPosition:{type:String,default:"left"}});var P,I=$(O),Z=n(7625),T=(0,f.d)("goods-action"),_=T[0],L=T[1],N=_({mixins:[(0,Z.G)("vanGoodsAction")],props:{safeAreaInsetBottom:{type:Boolean,default:!0}},render:function(){var t=arguments[0];return t("div",{class:L({unfit:!this.safeAreaInsetBottom})},[this.slots()])}}),R=(0,f.d)("goods-action-button"),A=R[0],E=R[1],F=A({mixins:[(0,Z.j)("vanGoodsAction")],props:(0,c.Z)({},x.g2,{type:String,text:String,icon:String,color:String,loading:Boolean,disabled:Boolean}),computed:{isFirst:function(){var t=this.parent&&this.parent.children[this.index-1];return!t||t.$options.name!==this.$options.name},isLast:function(){var t=this.parent&&this.parent.children[this.index+1];return!t||t.$options.name!==this.$options.name}},methods:{onClick:function(t){this.$emit("click",t),(0,x.BC)(this.$router,this)}},render:function(){var t=arguments[0];return t(I,{class:E([{first:this.isFirst,last:this.isLast},this.type]),attrs:{size:"large",type:this.type,icon:this.icon,color:this.color,loading:this.loading,disabled:this.disabled},on:{click:this.onClick}},[this.slots()||this.text])}}),D=(0,f.d)("dialog"),j=D[0],H=D[1],q=D[2],G=j({mixins:[(0,k.e)()],props:{title:String,theme:String,width:[Number,String],message:String,className:null,callback:Function,beforeClose:Function,messageAlign:String,cancelButtonText:String,cancelButtonColor:String,confirmButtonText:String,confirmButtonColor:String,showCancelButton:Boolean,overlay:{type:Boolean,default:!0},allowHtml:{type:Boolean,default:!0},transition:{type:String,default:"van-dialog-bounce"},showConfirmButton:{type:Boolean,default:!0},closeOnPopstate:{type:Boolean,default:!0},closeOnClickOverlay:{type:Boolean,default:!1}},data:function(){return{loading:{confirm:!1,cancel:!1}}},methods:{onClickOverlay:function(){this.handleAction("overlay")},handleAction:function(t){var e=this;this.$emit(t),this.value&&(this.beforeClose?(this.loading[t]=!0,this.beforeClose(t,(function(n){!1!==n&&e.loading[t]&&e.onClose(t),e.loading.confirm=!1,e.loading.cancel=!1}))):this.onClose(t))},onClose:function(t){this.close(),this.callback&&this.callback(t)},onOpened:function(){var t=this;this.$emit("opened"),this.$nextTick((function(){var e;null==(e=t.$refs.dialog)||e.focus()}))},onClosed:function(){this.$emit("closed")},onKeydown:function(t){var e=this;if("Escape"===t.key||"Enter"===t.key){if(t.target!==this.$refs.dialog)return;var n={Enter:this.showConfirmButton?function(){return e.handleAction("confirm")}:p.ZT,Escape:this.showCancelButton?function(){return e.handleAction("cancel")}:p.ZT};n[t.key](),this.$emit("keydown",t)}},genRoundButtons:function(){var t=this,e=this.$createElement;return e(N,{class:H("footer")},[this.showCancelButton&&e(F,{attrs:{size:"large",type:"warning",text:this.cancelButtonText||q("cancel"),color:this.cancelButtonColor,loading:this.loading.cancel},class:H("cancel"),on:{click:function(){t.handleAction("cancel")}}}),this.showConfirmButton&&e(F,{attrs:{size:"large",type:"danger",text:this.confirmButtonText||q("confirm"),color:this.confirmButtonColor,loading:this.loading.confirm},class:H("confirm"),on:{click:function(){t.handleAction("confirm")}}})])},genButtons:function(){var t,e=this,n=this.$createElement,o=this.showCancelButton&&this.showConfirmButton;return n("div",{class:[m,H("footer")]},[this.showCancelButton&&n(I,{attrs:{size:"large",loading:this.loading.cancel,text:this.cancelButtonText||q("cancel"),nativeType:"button"},class:H("cancel"),style:{color:this.cancelButtonColor},on:{click:function(){e.handleAction("cancel")}}}),this.showConfirmButton&&n(I,{attrs:{size:"large",loading:this.loading.confirm,text:this.confirmButtonText||q("confirm"),nativeType:"button"},class:[H("confirm"),(t={},t[y]=o,t)],style:{color:this.confirmButtonColor},on:{click:function(){e.handleAction("confirm")}}})])},genContent:function(t,e){var n=this.$createElement;if(e)return n("div",{class:H("content")},[e]);var o=this.message,i=this.messageAlign;if(o){var s,a,l={class:H("message",(s={"has-title":t},s[i]=i,s)),domProps:(a={},a[this.allowHtml?"innerHTML":"textContent"]=o,a)};return n("div",{class:H("content",{isolated:!t})},[n("div",h()([{},l]))])}}},render:function(){var t=arguments[0];if(this.shouldRender){var e=this.message,n=this.slots(),o=this.slots("title")||this.title,i=o&&t("div",{class:H("header",{isolated:!e&&!n})},[o]);return t("transition",{attrs:{name:this.transition},on:{afterEnter:this.onOpened,afterLeave:this.onClosed}},[t("div",{directives:[{name:"show",value:this.value}],attrs:{role:"dialog","aria-labelledby":this.title||e,tabIndex:0},class:[H([this.theme]),this.className],style:{width:(0,v.N)(this.width)},ref:"dialog",on:{keydown:this.onKeydown}},[i,this.genContent(o,n),"round-button"===this.theme?this.genRoundButtons():this.genButtons()])])}}});function M(t){return document.body.contains(t)}function K(){P&&P.$destroy(),P=new(u.ZP.extend(G))({el:document.createElement("div"),propsData:{lazyRender:!1}}),P.$on("input",(function(t){P.value=t}))}function U(t){return p.sk?Promise.resolve():new Promise((function(e,n){P&&M(P.$el)||K(),(0,c.Z)(P,U.currentOptions,t,{resolve:e,reject:n})}))}U.defaultOptions={value:!0,title:"",width:"",theme:null,message:"",overlay:!0,className:"",allowHtml:!0,lockScroll:!0,transition:"van-dialog-bounce",beforeClose:null,overlayClass:"",overlayStyle:null,messageAlign:"",getContainer:"body",cancelButtonText:"",cancelButtonColor:null,confirmButtonText:"",confirmButtonColor:null,showConfirmButton:!0,showCancelButton:!1,closeOnPopstate:!0,closeOnClickOverlay:!1,callback:function(t){P["confirm"===t?"resolve":"reject"](t)}},U.alert=U,U.confirm=function(t){return U((0,c.Z)({showCancelButton:!0},t))},U.close=function(){P&&(P.value=!1)},U.setDefaultOptions=function(t){(0,c.Z)(U.currentOptions,t)},U.resetDefaultOptions=function(){U.currentOptions=(0,c.Z)({},U.defaultOptions)},U.resetDefaultOptions(),U.install=function(){u.ZP.use(G)},U.Component=G,u.ZP.prototype.$dialog=U;var X=U,Y=function(t,e,n,o){var i,s=arguments.length,a=s<3?e:null===o?o=Object.getOwnPropertyDescriptor(e,n):o;if("object"===typeof Reflect&&"function"===typeof Reflect.decorate)a=Reflect.decorate(t,e,n,o);else for(var l=t.length-1;l>=0;l--)(i=t[l])&&(a=(s<3?i(a):s>3?i(e,n,a):i(e,n))||a);return s>3&&a&&Object.defineProperty(e,n,a),a};let J=class extends a.w3{constructor(...t){super(...t),(0,s.Z)(this,"loadFlag",!1),(0,s.Z)(this,"userInfo",{}),(0,s.Z)(this,"prizeList",[])}mounted(){this.getUserInfo()}getUserInfo(){r.Z.get("/user/info").then((t=>{if(1===t.data.code){const{data:e}=t.data;e.pic=""+e.pic,this.userInfo=e,this.getPrizeList()}else this.$router.push({name:"login"})}))}getPrizeList(){r.Z.get("/user/hit/-1/1/100").then((t=>{if(1===t.data.code){const{data:e}=t.data;e.totalNum&&e.items.length>0&&(this.prizeList=e.items),this.loadFlag=!0}}))}exitFun(){X.confirm({title:"温馨提示",message:"确定要退出登录吗？"}).then((()=>{r.Z.get("/logout").then((t=>{1===t.data.code&&this.$router.push({path:"/"})}))})).catch((()=>{}))}};J=Y([(0,a.wA)({components:{GoBack:l.Z}})],J);var Q=J,V=Q,W=n(1001),tt=(0,W.Z)(V,o,i,!1,null,"1962d503",null),et=tt.exports},4674:function(t,e,n){n.d(e,{e:function(){return Z}});n(560);var o={zIndex:2e3,lockCount:0,stack:[],find:function(t){return this.stack.filter((function(e){return e.vm===t}))[0]},remove:function(t){var e=this.find(t);if(e){e.vm=null,e.overlay=null;var n=this.stack.indexOf(e);this.stack.splice(n,1)}}},i=n(7378),s=n(2715),a=n.n(s),l=n(161),r=n(868),c=n(8745),u=n(6472),d=(0,l.d)("overlay"),h=d[0],f=d[1];function p(t){(0,u.PF)(t,!0)}function v(t,e,n,o){var s=(0,i.Z)({zIndex:e.zIndex},e.customStyle);return(0,r.Xq)(e.duration)&&(s.animationDuration=e.duration+"s"),t("transition",{attrs:{name:"van-fade"}},[t("div",a()([{directives:[{name:"show",value:e.show}],style:s,class:[f(),e.className],on:{touchmove:e.lockScroll?p:r.ZT}},(0,c.ED)(o,!0)]),[null==n.default?void 0:n.default()])])}v.props={show:Boolean,zIndex:[Number,String],duration:[Number,String],className:null,customStyle:Object,lockScroll:{type:Boolean,default:!0}};var g=h(v),m=n(3147),y={className:"",customStyle:{}};function C(t){return(0,c.LI)(g,{on:{click:function(){t.$emit("click-overlay"),t.closeOnClickOverlay&&(t.onClickOverlay?t.onClickOverlay():t.close())}}})}function k(t){var e=o.find(t);if(e){var n=t.$el,s=e.config,a=e.overlay;n&&n.parentNode&&n.parentNode.insertBefore(a.$el,n),(0,i.Z)(a,y,s,{show:!0})}}function B(t,e){var n=o.find(t);if(n)n.config=e;else{var i=C(t);o.stack.push({vm:t,config:e,overlay:i})}k(t)}function x(t){var e=o.find(t);e&&(e.overlay.show=!1)}function S(t){var e=o.find(t);e&&((0,m.Z)(e.overlay.$el),o.remove(t))}var b=n(5185),w=n(9579);function $(t){return"string"===typeof t?document.querySelector(t):t()}function z(t){var e=void 0===t?{}:t,n=e.ref,o=e.afterPortal;return{props:{getContainer:[String,Function]},watch:{getContainer:"portal"},mounted:function(){this.getContainer&&this.portal()},methods:{portal:function(){var t,e=this.getContainer,i=n?this.$refs[n]:this.$el;e?t=$(e):this.$parent&&(t=this.$parent.$el),t&&t!==i.parentNode&&t.appendChild(i),o&&o.call(this)}}}}var O=n(8886),P={mixins:[(0,O.X)((function(t,e){this.handlePopstate(e&&this.closeOnPopstate)}))],props:{closeOnPopstate:Boolean},data:function(){return{bindStatus:!1}},watch:{closeOnPopstate:function(t){this.handlePopstate(t)}},methods:{onPopstate:function(){this.close(),this.shouldReopen=!1},handlePopstate:function(t){if(!this.$isServer&&this.bindStatus!==t){this.bindStatus=t;var e=t?u.on:u.S1;e(window,"popstate",this.onPopstate)}}}},I={transitionAppear:Boolean,value:Boolean,overlay:Boolean,overlayStyle:Object,overlayClass:String,closeOnClickOverlay:Boolean,zIndex:[Number,String],lockScroll:{type:Boolean,default:!0},lazyRender:{type:Boolean,default:!0}};function Z(t){return void 0===t&&(t={}),{mixins:[w.D,P,z({afterPortal:function(){this.overlay&&k()}})],provide:function(){return{vanPopup:this}},props:I,data:function(){return this.onReopenCallback=[],{inited:this.value}},computed:{shouldRender:function(){return this.inited||!this.lazyRender}},watch:{value:function(e){var n=e?"open":"close";this.inited=this.inited||this.value,this[n](),t.skipToggleEvent||this.$emit(n)},overlay:"renderOverlay"},mounted:function(){this.value&&this.open()},activated:function(){this.shouldReopen&&(this.$emit("input",!0),this.shouldReopen=!1)},beforeDestroy:function(){S(this),this.opened&&this.removeLock(),this.getContainer&&(0,m.Z)(this.$el)},deactivated:function(){this.value&&(this.close(),this.shouldReopen=!0)},methods:{open:function(){this.$isServer||this.opened||(void 0!==this.zIndex&&(o.zIndex=this.zIndex),this.opened=!0,this.renderOverlay(),this.addLock(),this.onReopenCallback.forEach((function(t){t()})))},addLock:function(){this.lockScroll&&((0,u.on)(document,"touchstart",this.touchStart),(0,u.on)(document,"touchmove",this.onTouchMove),o.lockCount||document.body.classList.add("van-overflow-hidden"),o.lockCount++)},removeLock:function(){this.lockScroll&&o.lockCount&&(o.lockCount--,(0,u.S1)(document,"touchstart",this.touchStart),(0,u.S1)(document,"touchmove",this.onTouchMove),o.lockCount||document.body.classList.remove("van-overflow-hidden"))},close:function(){this.opened&&(x(this),this.opened=!1,this.removeLock(),this.$emit("input",!1))},onTouchMove:function(t){this.touchMove(t);var e=this.deltaY>0?"10":"01",n=(0,b.Ob)(t.target,this.$el),o=n.scrollHeight,i=n.offsetHeight,s=n.scrollTop,a="11";0===s?a=i>=o?"00":"01":s+i>=o&&(a="10"),"11"===a||"vertical"!==this.direction||parseInt(a,2)&parseInt(e,2)||(0,u.PF)(t,!0)},renderOverlay:function(){var t=this;!this.$isServer&&this.value&&this.$nextTick((function(){t.updateZIndex(t.overlay?1:0),t.overlay?B(t,{zIndex:o.zIndex++,duration:t.duration,className:t.overlayClass,customStyle:t.overlayStyle}):x(t)}))},updateZIndex:function(t){void 0===t&&(t=0),this.$el.style.zIndex=++o.zIndex+t},onReopen:function(t){this.onReopenCallback.push(t)}}}}},3147:function(t,e,n){function o(t){var e=t.parentNode;e&&e.removeChild(t)}n.d(e,{Z:function(){return o}})},8671:function(t,e,n){t.exports=n.p+"img/icon-01.94fc29dd.png"},6585:function(t,e,n){t.exports=n.p+"img/icon-02.ab0e04cd.png"}}]);
//# sourceMappingURL=681.148467a4.js.map