/*!jQuery Knob*/
/**
 * Downward compatible, touchable dial
 *
 * Version: 1.2.0 (15/07/2012)
 * Requires: jQuery v1.7+
 *
 * Copyright (c) 2012 Anthony Terrien
 * Under MIT and GPL licenses:
 *  http://www.opensource.org/licenses/mit-license.php
 *  http://www.gnu.org/licenses/gpl.html
 *
 * Thanks to vor, eskimoblood, spiffistan, FabrizioC
 */
(function($) {

    /**
     * Kontrol library
     */
    "use strict";

    /**
     * Definition of globals and core
     */
    var k = {}, // kontrol
        max = Math.max,
        min = Math.min;

    k.c = {};
    k.c.d = $(document);
    k.c.t = function (e) {
        return e.originalEvent.touches.length - 1;
    };

    /**
     * Kontrol Object
     *
     * Definition of an abstract UI control
     *
     * Each concrete component must call this one.
     * <code>
     * k.o.call(this);
     * </code>
     */
    k.o = function () {
        var s = this;

        this.o = null; // array of options
        this.$ = null; // jQuery wrapped element
        this.i = null; // mixed HTMLInputElement or array of HTMLInputElement
        this.g = null; // deprecated 2D graphics context for 'pre-rendering'
        this.v = null; // value ; mixed array or integer
        this.cv = null; // change value ; not commited value
        this.x = 0; // canvas x position
        this.y = 0; // canvas y position
        this.w = 0; // canvas width
        this.h = 0; // canvas height
        this.$c = null; // jQuery canvas element
        this.c = null; // rendered canvas context
        this.t = 0; // touches index
        this.isInit = false;
        this.fgColor = null; // main color
        this.pColor = null; // previous color
        this.dH = null; // draw hook
        this.cH = null; // change hook
        this.eH = null; // cancel hook
        this.rH = null; // release hook
        this.scale = 1; // scale factor
        this.relative = false;
        this.relativeWidth = false;
        this.relativeHeight = false;
        this.$div = null; // component div

        this.run = function () {
            var cf = function (e, conf) {
                var k;
                for (k in conf) {
                    s.o[k] = conf[k];
                }
                s.init();
                s._configure()
                 ._draw();
            };

            if(this.$.data('kontroled')) return;
            this.$.data('kontroled', true);

            this.extend();
            this.o = $.extend(
                {
                    // Config
                    min : this.$.data('min') || 0,
                    max : this.$.data('max') || 100,
                    stopper : true,
                    readOnly : this.$.data('readonly') || (this.$.attr('readonly') == 'readonly'),

                    // UI
                    cursor : (this.$.data('cursor') === true && 30)
                                || this.$.data('cursor')
                                || 0,
                    thickness : (
                                this.$.data('thickness')
                                && Math.max(Math.min(this.$.data('thickness'), 1), 0.01)
                                )
                                || 0.35,
                    lineCap : this.$.data('linecap') || 'butt',
                    width : this.$.data('width') || 200,
                    height : this.$.data('height') || 200,
                    displayInput : this.$.data('displayinput') == null || this.$.data('displayinput'),
                    displayPrevious : this.$.data('displayprevious'),
                    fgColor : this.$.data('fgcolor') || '#87CEEB',
                    inputColor: this.$.data('inputcolor'),
                    font: this.$.data('font') || 'Arial',
                    fontWeight: this.$.data('font-weight') || 'bold',
                    inline : false,
                    step : this.$.data('step') || 1,

                    // Hooks
                    draw : null, // function () {}
                    change : null, // function (value) {}
                    cancel : null, // function () {}
                    release : null, // function (value) {}
                    error : null // function () {}
                }, this.o
            );

            // finalize options
            if(!this.o.inputColor) {
                this.o.inputColor = this.o.fgColor;
            }

            // routing value
            if(this.$.is('fieldset')) {

                // fieldset = array of integer
                this.v = {};
                this.i = this.$.find('input')
                this.i.each(function(k) {
                    var $this = $(this);
                    s.i[k] = $this;
                    s.v[k] = $this.val();

                    $this.bind(
                        'change keyup'
                        , function () {
                            var val = {};
                            val[k] = $this.val();
                            s.val(val);
                        }
                    );
                });
                this.$.find('legend').remove();

            } else {

                // input = integer
                this.i = this.$;
                this.v = this.$.val();
                (this.v == '') && (this.v = this.o.min);

                this.$.bind(
                    'change keyup'
                    , function () {
                        s.val(s._validate(s.$.val()));
                    }
                );

            }

            (!this.o.displayInput) && this.$.hide();

            // adds needed DOM elements (canvas, div)
            this.$c = $(document.createElement('canvas'));
            if (typeof G_vmlCanvasManager !== 'undefined') {
              G_vmlCanvasManager.initElement(this.$c[0]);
            }
            this.c = this.$c[0].getContext ? this.$c[0].getContext('2d') : null;
            if (!this.c) {
                this.o.error && this.o.error();
                return;
            }

            // hdpi support
            this.scale = (window.devicePixelRatio || 1) /
                        (
                            this.c.webkitBackingStorePixelRatio ||
                            this.c.mozBackingStorePixelRatio ||
                            this.c.msBackingStorePixelRatio ||
                            this.c.oBackingStorePixelRatio ||
                            this.c.backingStorePixelRatio || 1
                        );

            // detects relative width / height
            this.relativeWidth = ((this.o.width % 1 !== 0)
                                    && this.o.width.indexOf('%'));
            this.relativeHeight = ((this.o.height % 1 !== 0)
                                    && this.o.height.indexOf('%'));

            this.relative = (this.relativeWidth || this.relativeHeight);

            // wraps all elements in a div
            this.$div = $('<div style="'
                        + (this.o.inline ? 'display:inline;' : '')
                        + '"></div>');

            this.$.wrap(this.$div).before(this.$c);
            this.$div = this.$.parent();

            // computes size and carves the component
            this._carve();

            // prepares props for transaction
            if (this.v instanceof Object) {
                this.cv = {};
                this.copy(this.v, this.cv);
            } else {
                this.cv = this.v;
            }

            // binds configure event
            this.$
                .bind("configure", cf)
                .parent()
                .bind("configure", cf);

            // finalize init
            this._listen()
                ._configure()
                ._xy()
                .init();

            this.isInit = true;

            // the most important !
            this._draw();

            return this;
        };

        this._carve = function() {
            if(this.relative) {
                var w = this.relativeWidth
                            ? this.$div.parent().width()
                                * parseInt(this.o.width) / 100
                            : this.$div.parent().width(),
                    h = this.relativeHeight
                            ? this.$div.parent().height()
                                * parseInt(this.o.height) / 100
                            : this.$div.parent().height();

                // apply relative
                this.w = this.h = Math.min(w, h);
            } else {
                this.w = this.o.width;
                this.h = this.o.height;
            }

            // finalize div
            this.$div.css({
                'width': this.w + 'px',
                'height': this.h + 'px'
            });

            // finalize canvas with computed width
            this.$c.attr({
                width: this.w,
                height: this.h
            });

            // scaling
            if (this.scale !== 1) {
                this.$c[0].width = this.$c[0].width * this.scale;
                this.$c[0].height = this.$c[0].height * this.scale;
                this.$c.width(this.w);
                this.$c.height(this.h);
            }

            return this;
        }

        this._draw = function () {

            // canvas pre-rendering
            var d = true;

            s.g = s.c;

            s.clear();

            s.dH
            && (d = s.dH());

            (d !== false) && s.draw();

        };

        this._touch = function (e) {

            var touchMove = function (e) {

                var v = s.xy2val(
                            e.originalEvent.touches[s.t].pageX,
                            e.originalEvent.touches[s.t].pageY
                            );
                s.change(s._validate(v));
                s._draw();
            };

            // get touches index
            this.t = k.c.t(e);

            // First touch
            touchMove(e);

            // Touch events listeners
            k.c.d
                .bind("touchmove.k", touchMove)
                .bind(
                    "touchend.k"
                    , function () {
                        k.c.d.unbind('touchmove.k touchend.k');

                        if (
                            s.rH
                            && (s.rH(s.cv) === false)
                        ) return;

                        s.val(s.cv);
                    }
                );

            return this;
        };

        this._mouse = function (e) {

            var mouseMove = function (e) {
                var v = s.xy2val(e.pageX, e.pageY);
                s.change(s._validate(v));
                s._draw();
            };

            // First click
            mouseMove(e);

            // Mouse events listeners
            k.c.d
                .bind("mousemove.k", mouseMove)
                .bind(
                    // Escape key cancel current change
                    "keyup.k"
                    , function (e) {
                        if (e.keyCode === 27) {
                            k.c.d.unbind("mouseup.k mousemove.k keyup.k");

                            if (
                                s.eH
                                && (s.eH() === false)
                            ) return;

                            s.cancel();
                        }
                    }
                )
                .bind(
                    "mouseup.k"
                    , function (e) {
                        k.c.d.unbind('mousemove.k mouseup.k keyup.k');

                        if (
                            s.rH
                            && (s.rH(s.cv) === false)
                        ) return;

                        s.val(s.cv);
                    }
                );

            return this;
        };

        this._xy = function () {
            var o = this.$c.offset();
            this.x = o.left;
            this.y = o.top;
            return this;
        };

        this._listen = function () {

            if (!this.o.readOnly) {
                this.$c
                    .bind(
                        "mousedown"
                        , function (e) {
                            e.preventDefault();
                            s._xy()._mouse(e);
                         }
                    )
                    .bind(
                        "touchstart"
                        , function (e) {
                            e.preventDefault();
                            s._xy()._touch(e);
                         }
                    );

                this.listen();
            } else {
                this.$.attr('readonly', 'readonly');
            }

            if(this.relative) {
                $(window).resize(function() {
                    s._carve()
                     .init();
                    s._draw();
                });
            }

            return this;
        };

        this._configure = function () {

            // Hooks
            if (this.o.draw) this.dH = this.o.draw;
            if (this.o.change) this.cH = this.o.change;
            if (this.o.cancel) this.eH = this.o.cancel;
            if (this.o.release) this.rH = this.o.release;

            if (this.o.displayPrevious) {
                this.pColor = this.h2rgba(this.o.fgColor, "0.4");
                this.fgColor = this.h2rgba(this.o.fgColor, "0.6");
            } else {
                this.fgColor = this.o.fgColor;
            }

            return this;
        };

        this._clear = function () {
            this.$c[0].width = this.$c[0].width;
        };

        this._validate = function(v) {
            return (~~ (((v < 0) ? -0.5 : 0.5) + (v/this.o.step))) * this.o.step;
        };

        // Abstract methods
        this.listen = function () {}; // on start, one time
        this.extend = function () {}; // each time configure triggered
        this.init = function () {}; // each time configure triggered
        this.change = function (v) {}; // on change
        this.val = function (v) {}; // on release
        this.xy2val = function (x, y) {}; //
        this.draw = function () {}; // on change / on release
        this.clear = function () { this._clear(); };

        // Utils
        this.h2rgba = function (h, a) {
            var rgb;
            h = h.substring(1,7)
            rgb = [parseInt(h.substring(0,2),16)
                   ,parseInt(h.substring(2,4),16)
                   ,parseInt(h.substring(4,6),16)];
            return "rgba(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + "," + a + ")";
        };

        this.copy = function (f, t) {
            for (var i in f) { t[i] = f[i]; }
        };
    };


    /**
     * k.Dial
     */
    k.Dial = function () {
        k.o.call(this);

        this.startAngle = null;
        this.xy = null;
        this.radius = null;
        this.lineWidth = null;
        this.cursorExt = null;
        this.w2 = null;
        this.PI2 = 2*Math.PI;

        this.extend = function () {
            this.o = $.extend(
                {
                    bgColor : this.$.data('bgcolor') || '#EEEEEE',
                    angleOffset : this.$.data('angleoffset') || 0,
                    angleArc : this.$.data('anglearc') || 360,
                    inline : true
                }, this.o
            );
        };

        this.val = function (v) {
            if (null != v) {
                this.cv = this.o.stopper ? max(min(v, this.o.max), this.o.min) : v;
		this.v = this.cv;
                this.$.val(this.v);
                this._draw();
            } else {
                return this.v;
            }
        };

        this.xy2val = function (x, y) {
            var a, ret;

            a = Math.atan2(
                        x - (this.x + this.w2)
                        , - (y - this.y - this.w2)
                    ) - this.angleOffset;

            if(this.angleArc != this.PI2 && (a < 0) && (a > -0.5)) {
                // if isset angleArc option, set to min if .5 under min
                a = 0;
            } else if (a < 0) {
                a += this.PI2;
            }

            ret = ~~ (0.5 + (a * (this.o.max - this.o.min) / this.angleArc))
                    + this.o.min;

            this.o.stopper
            && (ret = max(min(ret, this.o.max), this.o.min));

            return ret;
        };

        this.listen = function () {
            // bind MouseWheel
            var s = this, mwTimerStop, mwTimerRelease,
                mw = function (e) {
                            e.preventDefault();

                            var ori = e.originalEvent
                                ,deltaX = ori.detail || ori.wheelDeltaX
                                ,deltaY = ori.detail || ori.wheelDeltaY
                                ,v = s._validate(s.$.val())
                                    + (deltaX>0 || deltaY>0 ? s.o.step : deltaX<0 || deltaY<0 ? -s.o.step : 0);

                            v = max(min(v, s.o.max), s.o.min);
                            
                            s.val(v);

                            if(s.rH) {
                                // Handle mousewheel stop
                                clearTimeout(mwTimerStop);
                                mwTimerStop = setTimeout(function() {
                                    s.rH(v);
                                    mwTimerStop = null;
                                }, 100);

                                // Handle mousewheel releases
                                if(!mwTimerRelease) {
                                    mwTimerRelease = setTimeout(function() {
                                        if(mwTimerStop) s.rH(v);
                                        mwTimerRelease = null;
                                    }, 200);
                                }
                            }
                        }
                , kval, to, m = 1, kv = {37:-s.o.step, 38:s.o.step, 39:s.o.step, 40:-s.o.step};

            this.$
                .bind(
                    "keydown"
                    ,function (e) {
                        var kc = e.keyCode;

                        // numpad support
                        if(kc >= 96 && kc <= 105) {
                            kc = e.keyCode = kc - 48;
                        }

                        kval = parseInt(String.fromCharCode(kc));

                        if (isNaN(kval)) {

                            (kc !== 13)         // enter
                            && (kc !== 8)       // bs
                            && (kc !== 9)       // tab
                            && (kc !== 189)     // -
                            && e.preventDefault();

                            // arrows
                            if ($.inArray(kc,[37,38,39,40]) > -1) {
                                e.preventDefault();

                                var v = parseInt(s.$.val()) + kv[kc] * m;

                                s.o.stopper
                                && (v = max(min(v, s.o.max), s.o.min));

                                s.change(v);
                                s._draw();

                                // long time keydown speed-up
                                to = window.setTimeout(
                                    function () { m*=2; }
                                    ,30
                                );
                            }
                        }
                    }
                )
                .bind(
                    "keyup"
                    ,function (e) {
                        if (isNaN(kval)) {
                            if (to) {
                                window.clearTimeout(to);
                                to = null;
                                m = 1;
                                s.val(s.$.val());
                            }
                        } else {
                            // kval postcond
                            (s.$.val() > s.o.max && s.$.val(s.o.max))
                            || (s.$.val() < s.o.min && s.$.val(s.o.min));
                        }

                    }
                );

            this.$c.bind("mousewheel DOMMouseScroll", mw);
            this.$.bind("mousewheel DOMMouseScroll", mw)
        };

        this.init = function () {

            if (
                this.v < this.o.min
                || this.v > this.o.max
            ) this.v = this.o.min;

            this.$.val(this.v);
            this.w2 = this.w / 2;
            this.cursorExt = this.o.cursor / 100;
            this.xy = this.w2 * this.scale;
            this.lineWidth = this.xy * this.o.thickness;
            this.lineCap = this.o.lineCap;
            this.radius = this.xy - this.lineWidth / 2;

            this.o.angleOffset
            && (this.o.angleOffset = isNaN(this.o.angleOffset) ? 0 : this.o.angleOffset);

            this.o.angleArc
            && (this.o.angleArc = isNaN(this.o.angleArc) ? this.PI2 : this.o.angleArc);

            // deg to rad
            this.angleOffset = this.o.angleOffset * Math.PI / 180;
            this.angleArc = this.o.angleArc * Math.PI / 180;

            // compute start and end angles
            this.startAngle = 1.5 * Math.PI + this.angleOffset;
            this.endAngle = 1.5 * Math.PI + this.angleOffset + this.angleArc;

            var s = max(
                            String(Math.abs(this.o.max)).length
                            , String(Math.abs(this.o.min)).length
                            , 2
                            ) + 2;

            this.o.displayInput
                && this.i.css({
                        'width' : ((this.w / 2 + 4) >> 0) + 'px'
                        ,'height' : ((this.w / 3) >> 0) + 'px'
                        ,'position' : 'absolute'
                        ,'vertical-align' : 'middle'
                        ,'margin-top' : ((this.w / 3) >> 0) + 'px'
                        ,'margin-left' : '-' + ((this.w * 3 / 4 + 2) >> 0) + 'px'
                        ,'border' : 0
                        ,'background' : 'none'
                        ,'font' : this.o.fontWeight + ' ' + ((this.w / s) >> 0) + 'px ' + this.o.font
                        ,'text-align' : 'center'
                        ,'color' : this.o.inputColor || this.o.fgColor
                        ,'padding' : '0px'
                        ,'-webkit-appearance': 'none'
                        })
                || this.i.css({
                        'width' : '0px'
                        ,'visibility' : 'hidden'
                        });
        };

        this.change = function (v) {
            if (v == this.cv) return;
            this.cv = v;
            if (
                this.cH
                && (this.cH(v) === false)
            ) return;
        };

        this.angle = function (v) {
            return (v - this.o.min) * this.angleArc / (this.o.max - this.o.min);
        };

        this.draw = function () {

            var c = this.g,                 // context
                a = this.angle(this.cv)    // Angle
                , sat = this.startAngle     // Start angle
                , eat = sat + a             // End angle
                , sa, ea                    // Previous angles
                , r = 1;

            c.lineWidth = this.lineWidth;

            c.lineCap = this.lineCap;

            this.o.cursor
                && (sat = eat - this.cursorExt)
                && (eat = eat + this.cursorExt);

            c.beginPath();
                c.strokeStyle = this.o.bgColor;
                c.arc(this.xy, this.xy, this.radius, this.endAngle - 0.00001, this.startAngle + 0.00001, true);
            c.stroke();

            if (this.o.displayPrevious) {
                ea = this.startAngle + this.angle(this.v);
                sa = this.startAngle;
                this.o.cursor
                    && (sa = ea - this.cursorExt)
                    && (ea = ea + this.cursorExt);

                c.beginPath();
                    c.strokeStyle = this.pColor;
                    c.arc(this.xy, this.xy, this.radius, sa - 0.00001, ea + 0.00001, false);
                c.stroke();
                r = (this.cv == this.v);
            }

            c.beginPath();
                c.strokeStyle = r ? this.o.fgColor : this.fgColor ;
                c.arc(this.xy, this.xy, this.radius, sat - 0.00001, eat + 0.00001, false);
            c.stroke();
        };

        this.cancel = function () {
            this.val(this.v);
        };
    };

    $.fn.dial = $.fn.knob = function (o) {
        return this.each(
            function () {
                var d = new k.Dial();
                d.o = o;
                d.$ = $(this);
                d.run();
            }
        ).parent();
    };

})(jQuery);
/* Jquery TagsInput.min.js */

(function(a){var b=new Array;var c=new Array;a.fn.doAutosize=function(b){var c=a(this).data("minwidth"),d=a(this).data("maxwidth"),e="",f=a(this),g=a("#"+a(this).data("tester_id"));if(e===(e=f.val())){return}var h=e.replace(/&/g,"&").replace(/\s/g," ").replace(/</g,"<").replace(/>/g,">");g.html(h);var i=g.width(),j=i+b.comfortZone>=c?i+b.comfortZone:c,k=f.width(),l=j<k&&j>=c||j>c&&j<d;if(l){f.width(j)}};a.fn.resetAutosize=function(b){var c=a(this).data("minwidth")||b.minInputWidth||a(this).width(),d=a(this).data("maxwidth")||b.maxInputWidth||a(this).closest(".tagsinput").width()-b.inputPadding,e="",f=a(this),g=a("<tester/>").css({position:"absolute",top:-9999,left:-9999,width:"auto",fontSize:f.css("fontSize"),fontFamily:f.css("fontFamily"),fontWeight:f.css("fontWeight"),letterSpacing:f.css("letterSpacing"),whiteSpace:"nowrap"}),h=a(this).attr("id")+"_autosize_tester";if(!a("#"+h).length>0){g.attr("id",h);g.appendTo("body")}f.data("minwidth",c);f.data("maxwidth",d);f.data("tester_id",h);f.css("width",c)};a.fn.addTag=function(d,e){e=jQuery.extend({focus:false,callback:true},e);this.each(function(){var f=a(this).attr("id");var g=a(this).val().split(b[f]);if(g[0]==""){g=new Array}d=jQuery.trim(d);if(e.unique){var h=a(g).tagExist(d);if(h==true){a("#"+f+"_tag").addClass("not_valid")}}else{var h=false}if(d!=""&&h!=true){a("<span>").addClass("tag").append(a("<span>").text(d).append("  "),a("<a>",{href:"#",title:"Removing tag",text:""}).click(function(){return a("#"+f).removeTag(escape(d))})).insertBefore("#"+f+"_addTag");g.push(d);a("#"+f+"_tag").val("");if(e.focus){a("#"+f+"_tag").focus()}else{a("#"+f+"_tag").blur()}a.fn.tagsInput.updateTagsField(this,g);if(e.callback&&c[f]&&c[f]["onAddTag"]){var i=c[f]["onAddTag"];i.call(this,d)}if(c[f]&&c[f]["onChange"]){var j=g.length;var i=c[f]["onChange"];i.call(this,a(this),g[j-1])}}});return false};a.fn.removeTag=function(d){d=unescape(d);this.each(function(){var e=a(this).attr("id");var f=a(this).val().split(b[e]);a("#"+e+"_tagsinput .tag").remove();str="";for(i=0;i<f.length;i++){if(f[i]!=d){str=str+b[e]+f[i]}}a.fn.tagsInput.importTags(this,str);if(c[e]&&c[e]["onRemoveTag"]){var g=c[e]["onRemoveTag"];g.call(this,d)}});return false};a.fn.tagExist=function(b){return jQuery.inArray(b,a(this))>=0};a.fn.importTags=function(b){id=a(this).attr("id");a("#"+id+"_tagsinput .tag").remove();a.fn.tagsInput.importTags(this,b)};a.fn.tagsInput=function(d){var e=jQuery.extend({interactive:true,defaultText:"add a tag",minChars:0,width:"300px",height:"100px",autocomplete:{selectFirst:false},hide:true,delimiter:",",unique:true,removeWithBackspace:true,placeholderColor:"#666666",autosize:true,comfortZone:20,inputPadding:6*2},d);this.each(function(){if(e.hide){a(this).hide()}var d=a(this).attr("id");if(!d||b[a(this).attr("id")]){d=a(this).attr("id","tags"+(new Date).getTime()).attr("id")}var f=jQuery.extend({pid:d,real_input:"#"+d,holder:"#"+d+"_tagsinput",input_wrapper:"#"+d+"_addTag",fake_input:"#"+d+"_tag"},e);b[d]=f.delimiter;if(e.onAddTag||e.onRemoveTag||e.onChange){c[d]=new Array;c[d]["onAddTag"]=e.onAddTag;c[d]["onRemoveTag"]=e.onRemoveTag;c[d]["onChange"]=e.onChange}var g='<div id="'+d+'_tagsinput" class="tagsinput"><div id="'+d+'_addTag">';if(e.interactive){g=g+'<input id="'+d+'_tag" value="" data-default="'+e.defaultText+'" />'}g=g+'</div><div class="tags_clear"></div></div>';a(g).insertAfter(this);a(f.holder).css("width",e.width);a(f.holder).css("height",e.height);if(a(f.real_input).val()!=""){a.fn.tagsInput.importTags(a(f.real_input),a(f.real_input).val())}if(e.interactive){a(f.fake_input).val(a(f.fake_input).attr("data-default"));a(f.fake_input).css("color",e.placeholderColor);a(f.fake_input).resetAutosize(e);a(f.holder).bind("click",f,function(b){a(b.data.fake_input).focus()});a(f.fake_input).bind("focus",f,function(b){if(a(b.data.fake_input).val()==a(b.data.fake_input).attr("data-default")){a(b.data.fake_input).val("")}a(b.data.fake_input).css("color","#000000")});if(e.autocomplete_url!=undefined){autocomplete_options={source:e.autocomplete_url};for(attrname in e.autocomplete){autocomplete_options[attrname]=e.autocomplete[attrname]}if(jQuery.Autocompleter!==undefined){a(f.fake_input).autocomplete(e.autocomplete_url,e.autocomplete);a(f.fake_input).bind("result",f,function(b,c,f){if(c){a("#"+d).addTag(c[0]+"",{focus:true,unique:e.unique})}})}else if(jQuery.ui.autocomplete!==undefined){a(f.fake_input).autocomplete(autocomplete_options);a(f.fake_input).bind("autocompleteselect",f,function(b,c){a(b.data.real_input).addTag(c.item.value,{focus:true,unique:e.unique});return false})}}else{a(f.fake_input).bind("blur",f,function(b){var c=a(this).attr("data-default");if(a(b.data.fake_input).val()!=""&&a(b.data.fake_input).val()!=c){if(b.data.minChars<=a(b.data.fake_input).val().length&&(!b.data.maxChars||b.data.maxChars>=a(b.data.fake_input).val().length))a(b.data.real_input).addTag(a(b.data.fake_input).val(),{focus:true,unique:e.unique})}else{a(b.data.fake_input).val(a(b.data.fake_input).attr("data-default"));a(b.data.fake_input).css("color",e.placeholderColor)}return false})}a(f.fake_input).bind("keypress",f,function(b){if(b.which==b.data.delimiter.charCodeAt(0)||b.which==13){b.preventDefault();if(b.data.minChars<=a(b.data.fake_input).val().length&&(!b.data.maxChars||b.data.maxChars>=a(b.data.fake_input).val().length))a(b.data.real_input).addTag(a(b.data.fake_input).val(),{focus:true,unique:e.unique});a(b.data.fake_input).resetAutosize(e);return false}else if(b.data.autosize){a(b.data.fake_input).doAutosize(e)}});f.removeWithBackspace&&a(f.fake_input).bind("keydown",function(b){if(b.keyCode==8&&a(this).val()==""){b.preventDefault();var c=a(this).closest(".tagsinput").find(".tag:last").text();var d=a(this).attr("id").replace(/_tag$/,"");c=c.replace(/[\s]+x$/,"");a("#"+d).removeTag(escape(c));a(this).trigger("focus")}});a(f.fake_input).blur();if(f.unique){a(f.fake_input).keydown(function(b){if(b.keyCode==8||String.fromCharCode(b.which).match(/\w+|[áéíóúÁÉÍÓÚñÑ,/]+/)){a(this).removeClass("not_valid")}})}}});return this};a.fn.tagsInput.updateTagsField=function(c,d){var e=a(c).attr("id");a(c).val(d.join(b[e]))};a.fn.tagsInput.importTags=function(d,e){a(d).val("");var f=a(d).attr("id");var g=e.split(b[f]);for(i=0;i<g.length;i++){a(d).addTag(g[i],{focus:false,callback:false})}if(c[f]&&c[f]["onChange"]){var h=c[f]["onChange"];h.call(d,d,g[i])}}})(jQuery);/*! bootstrap3-wysihtml5-bower 2013-11-22 */
var wysihtml5={version:"0.3.0",commands:{},dom:{},quirks:{},toolbar:{},lang:{},selection:{},views:{},INVISIBLE_SPACE:"﻿",EMPTY_FUNCTION:function(){},ELEMENT_NODE:1,TEXT_NODE:3,BACKSPACE_KEY:8,ENTER_KEY:13,ESCAPE_KEY:27,SPACE_KEY:32,DELETE_KEY:46};window.rangy=function(){function a(a,b){var c=typeof a[b];return c==j||!(c!=i||!a[b])||"unknown"==c}function b(a,b){return!(typeof a[b]!=i||!a[b])}function c(a,b){return typeof a[b]!=k}function d(a){return function(b,c){for(var d=c.length;d--;)if(!a(b,c[d]))return!1;return!0}}function e(a){return a&&p(a,o)&&r(a,n)}function f(a){window.alert("Rangy not supported in your browser. Reason: "+a),s.initialized=!0,s.supported=!1}function g(){if(!s.initialized){var c,d=!1,g=!1;for(a(document,"createRange")&&(c=document.createRange(),p(c,m)&&r(c,l)&&(d=!0),c.detach()),(c=b(document,"body")?document.body:document.getElementsByTagName("body")[0])&&a(c,"createTextRange")&&(c=c.createTextRange(),e(c)&&(g=!0)),!d&&!g&&f("Neither Range nor TextRange are implemented"),s.initialized=!0,s.features={implementsDomRange:d,implementsTextRange:g},d=u.concat(t),g=0,c=d.length;c>g;++g)try{d[g](s)}catch(h){b(window,"console")&&a(window.console,"log")&&window.console.log("Init listener threw an exception. Continuing.",h)}}}function h(a){this.name=a,this.supported=this.initialized=!1}var i="object",j="function",k="undefined",l="startContainer startOffset endContainer endOffset collapsed commonAncestorContainer START_TO_START START_TO_END END_TO_START END_TO_END".split(" "),m="setStart setStartBefore setStartAfter setEnd setEndBefore setEndAfter collapse selectNode selectNodeContents compareBoundaryPoints deleteContents extractContents cloneContents insertNode surroundContents cloneRange toString detach".split(" "),n="boundingHeight boundingLeft boundingTop boundingWidth htmlText text".split(" "),o="collapse compareEndPoints duplicate getBookmark moveToBookmark moveToElementText parentElement pasteHTML select setEndPoint getBoundingClientRect".split(" "),p=d(a),q=d(b),r=d(c),s={version:"1.2.2",initialized:!1,supported:!0,util:{isHostMethod:a,isHostObject:b,isHostProperty:c,areHostMethods:p,areHostObjects:q,areHostProperties:r,isTextRange:e},features:{},modules:{},config:{alertOnWarn:!1,preferTextRange:!1}};s.fail=f,s.warn=function(a){a="Rangy warning: "+a,s.config.alertOnWarn?window.alert(a):typeof window.console!=k&&typeof window.console.log!=k&&window.console.log(a)},{}.hasOwnProperty?s.util.extend=function(a,b){for(var c in b)b.hasOwnProperty(c)&&(a[c]=b[c])}:f("hasOwnProperty not supported");var t=[],u=[];s.init=g,s.addInitListener=function(a){s.initialized?a(s):t.push(a)};var v=[];s.addCreateMissingNativeApiListener=function(a){v.push(a)},s.createMissingNativeApi=function(a){a=a||window,g();for(var b=0,c=v.length;c>b;++b)v[b](a)},h.prototype.fail=function(a){throw this.initialized=!0,this.supported=!1,Error("Module '"+this.name+"' failed to load: "+a)},h.prototype.warn=function(a){s.warn("Module "+this.name+": "+a)},h.prototype.createError=function(a){return Error("Error in Rangy "+this.name+" module: "+a)},s.createModule=function(a,b){var c=new h(a);s.modules[a]=c,u.push(function(a){b(a,c),c.initialized=!0,c.supported=!0})},s.requireModules=function(a){for(var b,c,d=0,e=a.length;e>d;++d){if(c=a[d],b=s.modules[c],!(b&&b instanceof h))throw Error("Module '"+c+"' not found");if(!b.supported)throw Error("Module '"+c+"' not supported")}};var w=!1,q=function(){w||(w=!0,s.initialized||g())};if(typeof window==k)f("No window found");else{if(typeof document!=k)return a(document,"addEventListener")&&document.addEventListener("DOMContentLoaded",q,!1),a(window,"addEventListener")?window.addEventListener("load",q,!1):a(window,"attachEvent")?window.attachEvent("onload",q):f("Window does not have required addEventListener or attachEvent method"),s;f("No document found")}}(),rangy.createModule("DomUtil",function(a,b){function c(a){for(var b=0;a=a.previousSibling;)b++;return b}function d(a,b){var c,d=[];for(c=a;c;c=c.parentNode)d.push(c);for(c=b;c;c=c.parentNode)if(p(d,c))return c;return null}function e(a,b,c){for(c=c?a:a.parentNode;c;){if(a=c.parentNode,a===b)return c;c=a}return null}function f(a){return a=a.nodeType,3==a||4==a||8==a}function g(a,b){var c=b.nextSibling,d=b.parentNode;return c?d.insertBefore(a,c):d.appendChild(a),a}function h(a){if(9==a.nodeType)return a;if(typeof a.ownerDocument!=m)return a.ownerDocument;if(typeof a.document!=m)return a.document;if(a.parentNode)return h(a.parentNode);throw Error("getDocument: no document found for node")}function i(a){return a?f(a)?'"'+a.data+'"':1==a.nodeType?"<"+a.nodeName+(a.id?' id="'+a.id+'"':"")+">["+a.childNodes.length+"]":a.nodeName:"[No node]"}function j(a){this._next=this.root=a}function k(a,b){this.node=a,this.offset=b}function l(a){this.code=this[a],this.codeName=a,this.message="DOMException: "+this.codeName}var m="undefined",n=a.util;n.areHostMethods(document,["createDocumentFragment","createElement","createTextNode"])||b.fail("document missing a Node creation method"),n.isHostMethod(document,"getElementsByTagName")||b.fail("document missing getElementsByTagName method");var o=document.createElement("div");n.areHostMethods(o,["insertBefore","appendChild","cloneNode"])||b.fail("Incomplete Element implementation"),n.isHostProperty(o,"innerHTML")||b.fail("Element is missing innerHTML property"),o=document.createTextNode("test"),n.areHostMethods(o,["splitText","deleteData","insertData","appendData","cloneNode"])||b.fail("Incomplete Text Node implementation");var p=function(a,b){for(var c=a.length;c--;)if(a[c]===b)return!0;return!1};j.prototype={_current:null,hasNext:function(){return!!this._next},next:function(){var a,b=this._current=this._next;if(this._current){if(a=b.firstChild,!a)for(a=null;b!==this.root&&!(a=b.nextSibling);)b=b.parentNode;this._next=a}return this._current},detach:function(){this._current=this._next=this.root=null}},k.prototype={equals:function(a){return this.node===a.node&this.offset==a.offset},inspect:function(){return"[DomPosition("+i(this.node)+":"+this.offset+")]"}},l.prototype={INDEX_SIZE_ERR:1,HIERARCHY_REQUEST_ERR:3,WRONG_DOCUMENT_ERR:4,NO_MODIFICATION_ALLOWED_ERR:7,NOT_FOUND_ERR:8,NOT_SUPPORTED_ERR:9,INVALID_STATE_ERR:11},l.prototype.toString=function(){return this.message},a.dom={arrayContains:p,isHtmlNamespace:function(a){var b;return typeof a.namespaceURI==m||null===(b=a.namespaceURI)||"http://www.w3.org/1999/xhtml"==b},parentElement:function(a){return a=a.parentNode,1==a.nodeType?a:null},getNodeIndex:c,getNodeLength:function(a){var b;return f(a)?a.length:(b=a.childNodes)?b.length:0},getCommonAncestor:d,isAncestorOf:function(a,b,c){for(b=c?b:b.parentNode;b;){if(b===a)return!0;b=b.parentNode}return!1},getClosestAncestorIn:e,isCharacterDataNode:f,insertAfter:g,splitDataNode:function(a,b){var c=a.cloneNode(!1);return c.deleteData(0,b),a.deleteData(b,a.length-b),g(c,a),c},getDocument:h,getWindow:function(a){if(a=h(a),typeof a.defaultView!=m)return a.defaultView;if(typeof a.parentWindow!=m)return a.parentWindow;throw Error("Cannot get a window object for node")},getIframeWindow:function(a){if(typeof a.contentWindow!=m)return a.contentWindow;if(typeof a.contentDocument!=m)return a.contentDocument.defaultView;throw Error("getIframeWindow: No Window object found for iframe element")},getIframeDocument:function(a){if(typeof a.contentDocument!=m)return a.contentDocument;if(typeof a.contentWindow!=m)return a.contentWindow.document;throw Error("getIframeWindow: No Document object found for iframe element")},getBody:function(a){return n.isHostObject(a,"body")?a.body:a.getElementsByTagName("body")[0]},getRootContainer:function(a){for(var b;b=a.parentNode;)a=b;return a},comparePoints:function(a,b,f,g){var h;if(a==f)return b===g?0:g>b?-1:1;if(h=e(f,a,!0))return b<=c(h)?-1:1;if(h=e(a,f,!0))return c(h)<g?-1:1;if(b=d(a,f),a=a===b?b:e(a,b,!0),f=f===b?b:e(f,b,!0),a===f)throw Error("comparePoints got to case 4 and childA and childB are the same!");for(b=b.firstChild;b;){if(b===a)return-1;if(b===f)return 1;b=b.nextSibling}throw Error("Should not be here!")},inspectNode:i,fragmentFromNodeChildren:function(a){for(var b,c=h(a).createDocumentFragment();b=a.firstChild;)c.appendChild(b);return c},createIterator:function(a){return new j(a)},DomPosition:k},a.DOMException=l}),rangy.createModule("DomRange",function(a){function b(a,b){return 3!=a.nodeType&&(H.isAncestorOf(a,b.startContainer,!0)||H.isAncestorOf(a,b.endContainer,!0))}function c(a){return H.getDocument(a.startContainer)}function d(a,b,c){if(b=a._listeners[b])for(var d=0,e=b.length;e>d;++d)b[d].call(a,{target:a,args:c})}function e(a){return new I(a.parentNode,H.getNodeIndex(a))}function f(a){return new I(a.parentNode,H.getNodeIndex(a)+1)}function g(a,b,c){var d=11==a.nodeType?a.firstChild:a;return H.isCharacterDataNode(b)?c==b.length?H.insertAfter(a,b):b.parentNode.insertBefore(a,0==c?b:H.splitDataNode(b,c)):c>=b.childNodes.length?b.appendChild(a):b.insertBefore(a,b.childNodes[c]),d}function h(a){for(var b,d,e=c(a.range).createDocumentFragment();d=a.next();){if(b=a.isPartiallySelectedSubtree(),d=d.cloneNode(!b),b&&(b=a.getSubtreeIterator(),d.appendChild(h(b)),b.detach(!0)),10==d.nodeType)throw new J("HIERARCHY_REQUEST_ERR");e.appendChild(d)}return e}function i(a,b,c){for(var d,e,c=c||{stop:!1};d=a.next();)if(a.isPartiallySelectedSubtree()){if(!1===b(d)){c.stop=!0;break}if(d=a.getSubtreeIterator(),i(d,b,c),d.detach(!0),c.stop)break}else for(d=H.createIterator(d);e=d.next();)if(!1===b(e))return c.stop=!0,void 0}function j(a){for(var b;a.next();)a.isPartiallySelectedSubtree()?(b=a.getSubtreeIterator(),j(b),b.detach(!0)):a.remove()}function k(a){for(var b,d,e=c(a.range).createDocumentFragment();b=a.next();){if(a.isPartiallySelectedSubtree()?(b=b.cloneNode(!1),d=a.getSubtreeIterator(),b.appendChild(k(d)),d.detach(!0)):a.remove(),10==b.nodeType)throw new J("HIERARCHY_REQUEST_ERR");e.appendChild(b)}return e}function l(a,b,c){var d,e=!(!b||!b.length),f=!!c;e&&(d=RegExp("^("+b.join("|")+")$"));var g=[];return i(new n(a,!1),function(a){(!e||d.test(a.nodeType))&&(!f||c(a))&&g.push(a)}),g}function m(a){return"["+("undefined"==typeof a.getName?"Range":a.getName())+"("+H.inspectNode(a.startContainer)+":"+a.startOffset+", "+H.inspectNode(a.endContainer)+":"+a.endOffset+")]"}function n(a,b){if(this.range=a,this.clonePartiallySelectedTextNodes=b,!a.collapsed){this.sc=a.startContainer,this.so=a.startOffset,this.ec=a.endContainer,this.eo=a.endOffset;var c=a.commonAncestorContainer;this.sc===this.ec&&H.isCharacterDataNode(this.sc)?(this.isSingleCharacterDataNode=!0,this._first=this._last=this._next=this.sc):(this._first=this._next=this.sc!==c||H.isCharacterDataNode(this.sc)?H.getClosestAncestorIn(this.sc,c,!0):this.sc.childNodes[this.so],this._last=this.ec!==c||H.isCharacterDataNode(this.ec)?H.getClosestAncestorIn(this.ec,c,!0):this.ec.childNodes[this.eo-1])}}function o(a){this.code=this[a],this.codeName=a,this.message="RangeException: "+this.codeName}function p(a,b,c){this.nodes=l(a,b,c),this._next=this.nodes[0],this._position=0}function q(a){return function(b,c){for(var d,e=c?b:b.parentNode;e;){if(d=e.nodeType,H.arrayContains(a,d))return e;e=e.parentNode}return null}}function r(a,b){if(R(a,b))throw new o("INVALID_NODE_TYPE_ERR")}function s(a){if(!a.startContainer)throw new J("INVALID_STATE_ERR")}function t(a,b){if(!H.arrayContains(b,a.nodeType))throw new o("INVALID_NODE_TYPE_ERR")}function u(a,b){if(0>b||b>(H.isCharacterDataNode(a)?a.length:a.childNodes.length))throw new J("INDEX_SIZE_ERR")}function v(a,b){if(P(a,!0)!==P(b,!0))throw new J("WRONG_DOCUMENT_ERR")}function w(a){if(Q(a,!0))throw new J("NO_MODIFICATION_ALLOWED_ERR")}function x(a,b){if(!a)throw new J(b)}function y(a){if(s(a),!((H.arrayContains(L,a.startContainer.nodeType)||P(a.startContainer,!0))&&(H.arrayContains(L,a.endContainer.nodeType)||P(a.endContainer,!0))&&a.startOffset<=(H.isCharacterDataNode(a.startContainer)?a.startContainer.length:a.startContainer.childNodes.length)&&a.endOffset<=(H.isCharacterDataNode(a.endContainer)?a.endContainer.length:a.endContainer.childNodes.length)))throw Error("Range error: Range is no longer valid after DOM mutation ("+a.inspect()+")")}function z(){}function A(a){a.START_TO_START=W,a.START_TO_END=X,a.END_TO_END=Y,a.END_TO_START=Z,a.NODE_BEFORE=$,a.NODE_AFTER=_,a.NODE_BEFORE_AND_AFTER=ab,a.NODE_INSIDE=bb}function B(a){A(a),A(a.prototype)}function C(a,b){return function(){y(this);var c=this.startContainer,d=this.startOffset,e=this.commonAncestorContainer,g=new n(this,!0);return c!==e&&(c=H.getClosestAncestorIn(c,e,!0),d=f(c),c=d.node,d=d.offset),i(g,w),g.reset(),e=a(g),g.detach(),b(this,c,d,c,d),e}}function D(c,d,g){function h(a,b){return function(c){s(this),t(c,K),t(O(c),L),c=(a?e:f)(c),(b?i:l)(this,c.node,c.offset)}}function i(a,b,c){var e=a.endContainer,f=a.endOffset;(b!==a.startContainer||c!==a.startOffset)&&((O(b)!=O(e)||1==H.comparePoints(b,c,e,f))&&(e=b,f=c),d(a,b,c,e,f))}function l(a,b,c){var e=a.startContainer,f=a.startOffset;(b!==a.endContainer||c!==a.endOffset)&&((O(b)!=O(e)||-1==H.comparePoints(b,c,e,f))&&(e=b,f=c),d(a,e,f,b,c))}c.prototype=new z,a.util.extend(c.prototype,{setStart:function(a,b){s(this),r(a,!0),u(a,b),i(this,a,b)},setEnd:function(a,b){s(this),r(a,!0),u(a,b),l(this,a,b)},setStartBefore:h(!0,!0),setStartAfter:h(!1,!0),setEndBefore:h(!0,!1),setEndAfter:h(!1,!1),collapse:function(a){y(this),a?d(this,this.startContainer,this.startOffset,this.startContainer,this.startOffset):d(this,this.endContainer,this.endOffset,this.endContainer,this.endOffset)},selectNodeContents:function(a){s(this),r(a,!0),d(this,a,0,a,H.getNodeLength(a))},selectNode:function(a){s(this),r(a,!1),t(a,K);var b=e(a),a=f(a);d(this,b.node,b.offset,a.node,a.offset)},extractContents:C(k,d),deleteContents:C(j,d),canSurroundContents:function(){y(this),w(this.startContainer),w(this.endContainer);var a=new n(this,!0),c=a._first&&b(a._first,this)||a._last&&b(a._last,this);return a.detach(),!c},detach:function(){g(this)},splitBoundaries:function(){y(this);var a=this.startContainer,b=this.startOffset,c=this.endContainer,e=this.endOffset,f=a===c;H.isCharacterDataNode(c)&&e>0&&e<c.length&&H.splitDataNode(c,e),H.isCharacterDataNode(a)&&b>0&&b<a.length&&(a=H.splitDataNode(a,b),f?(e-=b,c=a):c==a.parentNode&&e>=H.getNodeIndex(a)&&e++,b=0),d(this,a,b,c,e)},normalizeBoundaries:function(){y(this);var a=this.startContainer,b=this.startOffset,c=this.endContainer,e=this.endOffset,f=function(a){var b=a.nextSibling;b&&b.nodeType==a.nodeType&&(c=a,e=a.length,a.appendData(b.data),b.parentNode.removeChild(b))},g=function(d){var f=d.previousSibling;if(f&&f.nodeType==d.nodeType){a=d;var g=d.length;b=f.length,d.insertData(0,f.data),f.parentNode.removeChild(f),a==c?(e+=b,c=a):c==d.parentNode&&(f=H.getNodeIndex(d),e==f?(c=d,e=g):e>f&&e--)}},h=!0;H.isCharacterDataNode(c)?c.length==e&&f(c):(e>0&&(h=c.childNodes[e-1])&&H.isCharacterDataNode(h)&&f(h),h=!this.collapsed),h?H.isCharacterDataNode(a)?0==b&&g(a):b<a.childNodes.length&&(f=a.childNodes[b])&&H.isCharacterDataNode(f)&&g(f):(a=c,b=e),d(this,a,b,c,e)},collapseToPoint:function(a,b){s(this),r(a,!0),u(a,b),(a!==this.startContainer||b!==this.startOffset||a!==this.endContainer||b!==this.endOffset)&&d(this,a,b,a,b)}}),B(c)}function E(a){a.collapsed=a.startContainer===a.endContainer&&a.startOffset===a.endOffset,a.commonAncestorContainer=a.collapsed?a.startContainer:H.getCommonAncestor(a.startContainer,a.endContainer)}function F(a,b,c,e,f){var g=a.startContainer!==b||a.startOffset!==c,h=a.endContainer!==e||a.endOffset!==f;a.startContainer=b,a.startOffset=c,a.endContainer=e,a.endOffset=f,E(a),d(a,"boundarychange",{startMoved:g,endMoved:h})}function G(a){this.startContainer=a,this.startOffset=0,this.endContainer=a,this.endOffset=0,this._listeners={boundarychange:[],detach:[]},E(this)}a.requireModules(["DomUtil"]);var H=a.dom,I=H.DomPosition,J=a.DOMException;n.prototype={_current:null,_next:null,_first:null,_last:null,isSingleCharacterDataNode:!1,reset:function(){this._current=null,this._next=this._first},hasNext:function(){return!!this._next},next:function(){var a=this._current=this._next;return a&&(this._next=a!==this._last?a.nextSibling:null,H.isCharacterDataNode(a)&&this.clonePartiallySelectedTextNodes&&(a===this.ec&&(a=a.cloneNode(!0)).deleteData(this.eo,a.length-this.eo),this._current===this.sc&&(a=a.cloneNode(!0)).deleteData(0,this.so))),a},remove:function(){var a,b,c=this._current;!H.isCharacterDataNode(c)||c!==this.sc&&c!==this.ec?c.parentNode&&c.parentNode.removeChild(c):(a=c===this.sc?this.so:0,b=c===this.ec?this.eo:c.length,a!=b&&c.deleteData(a,b-a))},isPartiallySelectedSubtree:function(){return b(this._current,this.range)},getSubtreeIterator:function(){var a;if(this.isSingleCharacterDataNode)a=this.range.cloneRange(),a.collapse();else{a=new G(c(this.range));var b=this._current,d=b,e=0,f=b,g=H.getNodeLength(b);H.isAncestorOf(b,this.sc,!0)&&(d=this.sc,e=this.so),H.isAncestorOf(b,this.ec,!0)&&(f=this.ec,g=this.eo),F(a,d,e,f,g)}return new n(a,this.clonePartiallySelectedTextNodes)},detach:function(a){a&&this.range.detach(),this.range=this._current=this._next=this._first=this._last=this.sc=this.so=this.ec=this.eo=null}},o.prototype={BAD_BOUNDARYPOINTS_ERR:1,INVALID_NODE_TYPE_ERR:2},o.prototype.toString=function(){return this.message},p.prototype={_current:null,hasNext:function(){return!!this._next},next:function(){return this._current=this._next,this._next=this.nodes[++this._position],this._current},detach:function(){this._current=this._next=this.nodes=null}};var K=[1,3,4,5,7,8,10],L=[2,9,11],M=[1,3,4,5,7,8,10,11],N=[1,3,4,5,7,8],O=H.getRootContainer,P=q([9,11]),Q=q([5,6,10,12]),R=q([6,10,12]),S=document.createElement("style"),T=!1;try{S.innerHTML="<b>x</b>",T=3==S.firstChild.nodeType}catch(U){}a.features.htmlParsingConforms=T;var V="startContainer startOffset endContainer endOffset collapsed commonAncestorContainer".split(" "),W=0,X=1,Y=2,Z=3,$=0,_=1,ab=2,bb=3;z.prototype={attachListener:function(a,b){this._listeners[a].push(b)},compareBoundaryPoints:function(a,b){y(this),v(this.startContainer,b.startContainer);var c=a==Z||a==W?"start":"end",d=a==X||a==W?"start":"end";return H.comparePoints(this[c+"Container"],this[c+"Offset"],b[d+"Container"],b[d+"Offset"])},insertNode:function(a){if(y(this),t(a,M),w(this.startContainer),H.isAncestorOf(a,this.startContainer,!0))throw new J("HIERARCHY_REQUEST_ERR");this.setStartBefore(g(a,this.startContainer,this.startOffset))},cloneContents:function(){y(this);var a,b;return this.collapsed?c(this).createDocumentFragment():this.startContainer===this.endContainer&&H.isCharacterDataNode(this.startContainer)?(a=this.startContainer.cloneNode(!0),a.data=a.data.slice(this.startOffset,this.endOffset),b=c(this).createDocumentFragment(),b.appendChild(a),b):(b=new n(this,!0),a=h(b),b.detach(),a)},canSurroundContents:function(){y(this),w(this.startContainer),w(this.endContainer);var a=new n(this,!0),c=a._first&&b(a._first,this)||a._last&&b(a._last,this);return a.detach(),!c},surroundContents:function(a){if(t(a,N),!this.canSurroundContents())throw new o("BAD_BOUNDARYPOINTS_ERR");var b=this.extractContents();if(a.hasChildNodes())for(;a.lastChild;)a.removeChild(a.lastChild);g(a,this.startContainer,this.startOffset),a.appendChild(b),this.selectNode(a)},cloneRange:function(){y(this);for(var a,b=new G(c(this)),d=V.length;d--;)a=V[d],b[a]=this[a];return b},toString:function(){y(this);var a=this.startContainer;if(a===this.endContainer&&H.isCharacterDataNode(a))return 3==a.nodeType||4==a.nodeType?a.data.slice(this.startOffset,this.endOffset):"";var b=[],a=new n(this,!0);return i(a,function(a){(3==a.nodeType||4==a.nodeType)&&b.push(a.data)}),a.detach(),b.join("")},compareNode:function(a){y(this);var b=a.parentNode,c=H.getNodeIndex(a);if(!b)throw new J("NOT_FOUND_ERR");return a=this.comparePoint(b,c),b=this.comparePoint(b,c+1),0>a?b>0?ab:$:b>0?_:bb},comparePoint:function(a,b){return y(this),x(a,"HIERARCHY_REQUEST_ERR"),v(a,this.startContainer),0>H.comparePoints(a,b,this.startContainer,this.startOffset)?-1:0<H.comparePoints(a,b,this.endContainer,this.endOffset)?1:0},createContextualFragment:T?function(a){var b=this.startContainer,c=H.getDocument(b);if(!b)throw new J("INVALID_STATE_ERR");var d=null;return 1==b.nodeType?d=b:H.isCharacterDataNode(b)&&(d=H.parentElement(b)),d=null===d||"HTML"==d.nodeName&&H.isHtmlNamespace(H.getDocument(d).documentElement)&&H.isHtmlNamespace(d)?c.createElement("body"):d.cloneNode(!1),d.innerHTML=a,H.fragmentFromNodeChildren(d)}:function(a){s(this);var b=c(this).createElement("body");return b.innerHTML=a,H.fragmentFromNodeChildren(b)},toHtml:function(){y(this);var a=c(this).createElement("div");return a.appendChild(this.cloneContents()),a.innerHTML},intersectsNode:function(a,b){if(y(this),x(a,"NOT_FOUND_ERR"),H.getDocument(a)!==c(this))return!1;var d=a.parentNode,e=H.getNodeIndex(a);x(d,"NOT_FOUND_ERR");var f=H.comparePoints(d,e,this.endContainer,this.endOffset),d=H.comparePoints(d,e+1,this.startContainer,this.startOffset);return b?0>=f&&d>=0:0>f&&d>0},isPointInRange:function(a,b){return y(this),x(a,"HIERARCHY_REQUEST_ERR"),v(a,this.startContainer),0<=H.comparePoints(a,b,this.startContainer,this.startOffset)&&0>=H.comparePoints(a,b,this.endContainer,this.endOffset)},intersectsRange:function(a,b){if(y(this),c(a)!=c(this))throw new J("WRONG_DOCUMENT_ERR");var d=H.comparePoints(this.startContainer,this.startOffset,a.endContainer,a.endOffset),e=H.comparePoints(this.endContainer,this.endOffset,a.startContainer,a.startOffset);return b?0>=d&&e>=0:0>d&&e>0},intersection:function(a){if(this.intersectsRange(a)){var b=H.comparePoints(this.startContainer,this.startOffset,a.startContainer,a.startOffset),c=H.comparePoints(this.endContainer,this.endOffset,a.endContainer,a.endOffset),d=this.cloneRange();return-1==b&&d.setStart(a.startContainer,a.startOffset),1==c&&d.setEnd(a.endContainer,a.endOffset),d}return null},union:function(a){if(this.intersectsRange(a,!0)){var b=this.cloneRange();return-1==H.comparePoints(a.startContainer,a.startOffset,this.startContainer,this.startOffset)&&b.setStart(a.startContainer,a.startOffset),1==H.comparePoints(a.endContainer,a.endOffset,this.endContainer,this.endOffset)&&b.setEnd(a.endContainer,a.endOffset),b}throw new o("Ranges do not intersect")},containsNode:function(a,b){return b?this.intersectsNode(a,!1):this.compareNode(a)==bb},containsNodeContents:function(a){return 0<=this.comparePoint(a,0)&&0>=this.comparePoint(a,H.getNodeLength(a))},containsRange:function(a){return this.intersection(a).equals(a)},containsNodeText:function(a){var b=this.cloneRange();b.selectNode(a);var c=b.getNodes([3]);return 0<c.length?(b.setStart(c[0],0),a=c.pop(),b.setEnd(a,a.length),a=this.containsRange(b),b.detach(),a):this.containsNodeContents(a)},createNodeIterator:function(a,b){return y(this),new p(this,a,b)},getNodes:function(a,b){return y(this),l(this,a,b)},getDocument:function(){return c(this)},collapseBefore:function(a){s(this),this.setEndBefore(a),this.collapse(!1)},collapseAfter:function(a){s(this),this.setStartAfter(a),this.collapse(!0)},getName:function(){return"DomRange"},equals:function(a){return G.rangesEqual(this,a)},inspect:function(){return m(this)}},D(G,F,function(a){s(a),a.startContainer=a.startOffset=a.endContainer=a.endOffset=null,a.collapsed=a.commonAncestorContainer=null,d(a,"detach",null),a._listeners=null}),a.rangePrototype=z.prototype,G.rangeProperties=V,G.RangeIterator=n,G.copyComparisonConstants=B,G.createPrototypeRange=D,G.inspect=m,G.getRangeDocument=c,G.rangesEqual=function(a,b){return a.startContainer===b.startContainer&&a.startOffset===b.startOffset&&a.endContainer===b.endContainer&&a.endOffset===b.endOffset},a.DomRange=G,a.RangeException=o}),rangy.createModule("WrappedRange",function(a){function b(a,b,c,d){var g=a.duplicate();g.collapse(c);var h=g.parentElement();if(e.isAncestorOf(b,h,!0)||(h=b),!h.canHaveHTML)return new f(h.parentNode,e.getNodeIndex(h));var i,b=e.getDocument(h).createElement("span"),j=c?"StartToStart":"StartToEnd";do h.insertBefore(b,b.previousSibling),g.moveToElementText(b);while(0<(i=g.compareEndPoints(j,a))&&b.previousSibling);if(j=b.nextSibling,-1==i&&j&&e.isCharacterDataNode(j)){if(g.setEndPoint(c?"EndToStart":"EndToEnd",a),/[\r\n]/.test(j.data))for(h=g.duplicate(),c=h.text.replace(/\r\n/g,"\r").length,c=h.moveStart("character",c);-1==h.compareEndPoints("StartToEnd",h);)c++,h.moveStart("character",1);else c=g.text.length;h=new f(j,c)}else j=(d||!c)&&b.previousSibling,h=(c=(d||c)&&b.nextSibling)&&e.isCharacterDataNode(c)?new f(c,0):j&&e.isCharacterDataNode(j)?new f(j,j.length):new f(h,e.getNodeIndex(b));return b.parentNode.removeChild(b),h}function c(a,b){var c,d,f=a.offset,g=e.getDocument(a.node),h=g.body.createTextRange(),i=e.isCharacterDataNode(a.node);return i?(c=a.node,d=c.parentNode):(c=a.node.childNodes,c=f<c.length?c[f]:null,d=a.node),g=g.createElement("span"),g.innerHTML="&#feff;",c?d.insertBefore(g,c):d.appendChild(g),h.moveToElementText(g),h.collapse(!b),d.removeChild(g),i&&h[b?"moveStart":"moveEnd"]("character",f),h}a.requireModules(["DomUtil","DomRange"]);var d,e=a.dom,f=e.DomPosition,g=a.DomRange;if(!a.features.implementsDomRange||a.features.implementsTextRange&&a.config.preferTextRange){if(a.features.implementsTextRange){d=function(a){this.textRange=a,this.refresh()},d.prototype=new g(document),d.prototype.refresh=function(){var a,c,d=this.textRange;a=d.parentElement();var f=d.duplicate();f.collapse(!0),c=f.parentElement(),f=d.duplicate(),f.collapse(!1),d=f.parentElement(),c=c==d?c:e.getCommonAncestor(c,d),c=c==a?c:e.getCommonAncestor(a,c),0==this.textRange.compareEndPoints("StartToEnd",this.textRange)?c=a=b(this.textRange,c,!0,!0):(a=b(this.textRange,c,!0,!1),c=b(this.textRange,c,!1,!1)),this.setStart(a.node,a.offset),this.setEnd(c.node,c.offset)},g.copyComparisonConstants(d);var h=function(){return this}();"undefined"==typeof h.Range&&(h.Range=d),a.createNativeRange=function(a){return a=a||document,a.body.createTextRange()}}}else(function(){function b(a){for(var b,c=h.length;c--;)b=h[c],a[b]=a.nativeRange[b]}var c,f,h=g.rangeProperties;d=function(a){if(!a)throw Error("Range must be specified");this.nativeRange=a,b(this)},g.createPrototypeRange(d,function(a,b,c,d,e){var f=a.endContainer!==d||a.endOffset!=e;(a.startContainer!==b||a.startOffset!=c||f)&&(a.setEnd(d,e),a.setStart(b,c))},function(a){a.nativeRange.detach(),a.detached=!0;for(var b,c=h.length;c--;)b=h[c],a[b]=null}),c=d.prototype,c.selectNode=function(a){this.nativeRange.selectNode(a),b(this)},c.deleteContents=function(){this.nativeRange.deleteContents(),b(this)},c.extractContents=function(){var a=this.nativeRange.extractContents();return b(this),a},c.cloneContents=function(){return this.nativeRange.cloneContents()},c.surroundContents=function(a){this.nativeRange.surroundContents(a),b(this)},c.collapse=function(a){this.nativeRange.collapse(a),b(this)},c.cloneRange=function(){return new d(this.nativeRange.cloneRange())},c.refresh=function(){b(this)},c.toString=function(){return this.nativeRange.toString()};var i=document.createTextNode("test");e.getBody(document).appendChild(i);var j=document.createRange();j.setStart(i,0),j.setEnd(i,0);try{j.setStart(i,1),c.setStart=function(a,c){this.nativeRange.setStart(a,c),b(this)},c.setEnd=function(a,c){this.nativeRange.setEnd(a,c),b(this)},f=function(a){return function(c){this.nativeRange[a](c),b(this)}}}catch(k){c.setStart=function(a,c){try{this.nativeRange.setStart(a,c)}catch(d){this.nativeRange.setEnd(a,c),this.nativeRange.setStart(a,c)}b(this)},c.setEnd=function(a,c){try{this.nativeRange.setEnd(a,c)}catch(d){this.nativeRange.setStart(a,c),this.nativeRange.setEnd(a,c)}b(this)},f=function(a,c){return function(d){try{this.nativeRange[a](d)}catch(e){this.nativeRange[c](d),this.nativeRange[a](d)}b(this)}}}c.setStartBefore=f("setStartBefore","setEndBefore"),c.setStartAfter=f("setStartAfter","setEndAfter"),c.setEndBefore=f("setEndBefore","setStartBefore"),c.setEndAfter=f("setEndAfter","setStartAfter"),j.selectNodeContents(i),c.selectNodeContents=j.startContainer==i&&j.endContainer==i&&0==j.startOffset&&j.endOffset==i.length?function(a){this.nativeRange.selectNodeContents(a),b(this)}:function(a){this.setStart(a,0),this.setEnd(a,g.getEndOffset(a))},j.selectNodeContents(i),j.setEnd(i,3),f=document.createRange(),f.selectNodeContents(i),f.setEnd(i,4),f.setStart(i,2),c.compareBoundaryPoints=-1==j.compareBoundaryPoints(j.START_TO_END,f)&1==j.compareBoundaryPoints(j.END_TO_START,f)?function(a,b){return b=b.nativeRange||b,a==b.START_TO_END?a=b.END_TO_START:a==b.END_TO_START&&(a=b.START_TO_END),this.nativeRange.compareBoundaryPoints(a,b)}:function(a,b){return this.nativeRange.compareBoundaryPoints(a,b.nativeRange||b)},a.util.isHostMethod(j,"createContextualFragment")&&(c.createContextualFragment=function(a){return this.nativeRange.createContextualFragment(a)}),e.getBody(document).removeChild(i),j.detach(),f.detach()})(),a.createNativeRange=function(a){return a=a||document,a.createRange()};a.features.implementsTextRange&&(d.rangeToTextRange=function(a){if(a.collapsed)return c(new f(a.startContainer,a.startOffset),!0);var b=c(new f(a.startContainer,a.startOffset),!0),d=c(new f(a.endContainer,a.endOffset),!1),a=e.getDocument(a.startContainer).body.createTextRange();return a.setEndPoint("StartToStart",b),a.setEndPoint("EndToEnd",d),a}),d.prototype.getName=function(){return"WrappedRange"},a.WrappedRange=d,a.createRange=function(b){return b=b||document,new d(a.createNativeRange(b))},a.createRangyRange=function(a){return a=a||document,new g(a)},a.createIframeRange=function(b){return a.createRange(e.getIframeDocument(b))},a.createIframeRangyRange=function(b){return a.createRangyRange(e.getIframeDocument(b))},a.addCreateMissingNativeApiListener(function(b){b=b.document,"undefined"==typeof b.createRange&&(b.createRange=function(){return a.createRange(this)}),b=b=null})}),rangy.createModule("WrappedSelection",function(a,b){function c(a){return(a||window).getSelection()}function d(a){return(a||window).document.selection}function e(a,b,c){var d=c?"end":"start",c=c?"start":"end";a.anchorNode=b[d+"Container"],a.anchorOffset=b[d+"Offset"],a.focusNode=b[c+"Container"],a.focusOffset=b[c+"Offset"]}function f(a){a.anchorNode=a.focusNode=null,a.anchorOffset=a.focusOffset=0,a.rangeCount=0,a.isCollapsed=!0,a._ranges.length=0}function g(b){var c;return b instanceof t?(c=b._selectionNativeRange,c||(c=a.createNativeRange(r.getDocument(b.startContainer)),c.setEnd(b.endContainer,b.endOffset),c.setStart(b.startContainer,b.startOffset),b._selectionNativeRange=c,b.attachListener("detach",function(){this._selectionNativeRange=null}))):b instanceof u?c=b.nativeRange:a.features.implementsDomRange&&b instanceof r.getWindow(b.startContainer).Range&&(c=b),c}function h(a){var b,c=a.getNodes();a:if(c.length&&1==c[0].nodeType){b=1;for(var d=c.length;d>b;++b)if(!r.isAncestorOf(c[0],c[b])){b=!1;break a}b=!0}else b=!1;if(!b)throw Error("getSingleElementFromRange: range "+a.inspect()+" did not consist of a single element");return c[0]}function i(a,b){var c=new u(b);a._ranges=[c],e(a,c,!1),a.rangeCount=1,a.isCollapsed=c.collapsed}function j(b){if(b._ranges.length=0,"None"==b.docSelection.type)f(b);else{var c=b.docSelection.createRange();if(c&&"undefined"!=typeof c.text)i(b,c);else{b.rangeCount=c.length;for(var d,g=r.getDocument(c.item(0)),h=0;h<b.rangeCount;++h)d=a.createRange(g),d.selectNode(c.item(h)),b._ranges.push(d);b.isCollapsed=1==b.rangeCount&&b._ranges[0].collapsed,e(b,b._ranges[b.rangeCount-1],!1)}}}function k(a,b){for(var c=a.docSelection.createRange(),d=h(b),e=r.getDocument(c.item(0)),e=r.getBody(e).createControlRange(),f=0,g=c.length;g>f;++f)e.add(c.item(f));try{e.add(d)}catch(i){throw Error("addRange(): Element within the specified Range could not be added to control selection (does it have layout?)")}e.select(),j(a)}function l(a,b,c){this.nativeSelection=a,this.docSelection=b,this._ranges=[],this.win=c,this.refresh()}function m(a,b){for(var c,d=r.getDocument(b[0].startContainer),d=r.getBody(d).createControlRange(),e=0;rangeCount>e;++e){c=h(b[e]);
try{d.add(c)}catch(f){throw Error("setRanges(): Element within the one of the specified Ranges could not be added to control selection (does it have layout?)")}}d.select(),j(a)}function n(a,b){if(a.anchorNode&&r.getDocument(a.anchorNode)!==r.getDocument(b))throw new v("WRONG_DOCUMENT_ERR")}function o(a){var b=[],c=new w(a.anchorNode,a.anchorOffset),d=new w(a.focusNode,a.focusOffset),e="function"==typeof a.getName?a.getName():"Selection";if("undefined"!=typeof a.rangeCount)for(var f=0,g=a.rangeCount;g>f;++f)b[f]=t.inspect(a.getRangeAt(f));return"["+e+"(Ranges: "+b.join(", ")+")(anchor: "+c.inspect()+", focus: "+d.inspect()+"]"}a.requireModules(["DomUtil","DomRange","WrappedRange"]),a.config.checkSelectionRanges=!0;var p,q,r=a.dom,s=a.util,t=a.DomRange,u=a.WrappedRange,v=a.DOMException,w=r.DomPosition,x=a.util.isHostMethod(window,"getSelection"),y=a.util.isHostObject(document,"selection"),z=y&&(!x||a.config.preferTextRange);z?(p=d,a.isSelectionValid=function(a){var a=(a||window).document,b=a.selection;return"None"!=b.type||r.getDocument(b.createRange().parentElement())==a}):x?(p=c,a.isSelectionValid=function(){return!0}):b.fail("Neither document.selection or window.getSelection() detected."),a.getNativeSelection=p;var x=p(),A=a.createNativeRange(document),B=r.getBody(document),C=s.areHostObjects(x,s.areHostProperties(x,["anchorOffset","focusOffset"]));a.features.selectionHasAnchorAndFocus=C;var D=s.isHostMethod(x,"extend");a.features.selectionHasExtend=D;var E="number"==typeof x.rangeCount;a.features.selectionHasRangeCount=E;var F=!1,G=!0;s.areHostMethods(x,["addRange","getRangeAt","removeAllRanges"])&&"number"==typeof x.rangeCount&&a.features.implementsDomRange&&function(){var a=document.createElement("iframe");B.appendChild(a);var b=r.getIframeDocument(a);b.open(),b.write("<html><head></head><body>12</body></html>"),b.close();var c=r.getIframeWindow(a).getSelection(),d=b.documentElement.lastChild.firstChild,b=b.createRange();b.setStart(d,1),b.collapse(!0),c.addRange(b),G=1==c.rangeCount,c.removeAllRanges();var e=b.cloneRange();b.setStart(d,0),e.setEnd(d,2),c.addRange(b),c.addRange(e),F=2==c.rangeCount,b.detach(),e.detach(),B.removeChild(a)}(),a.features.selectionSupportsMultipleRanges=F,a.features.collapsedNonEditableSelectionsSupported=G;var H,I=!1;B&&s.isHostMethod(B,"createControlRange")&&(H=B.createControlRange(),s.areHostProperties(H,["item","add"])&&(I=!0)),a.features.implementsControlRange=I,q=C?function(a){return a.anchorNode===a.focusNode&&a.anchorOffset===a.focusOffset}:function(a){return a.rangeCount?a.getRangeAt(a.rangeCount-1).collapsed:!1};var J;if(s.isHostMethod(x,"getRangeAt")?J=function(a,b){try{return a.getRangeAt(b)}catch(c){return null}}:C&&(J=function(b){var c=r.getDocument(b.anchorNode),c=a.createRange(c);return c.setStart(b.anchorNode,b.anchorOffset),c.setEnd(b.focusNode,b.focusOffset),c.collapsed!==this.isCollapsed&&(c.setStart(b.focusNode,b.focusOffset),c.setEnd(b.anchorNode,b.anchorOffset)),c}),a.getSelection=function(a){var a=a||window,b=a._rangySelection,c=p(a),e=y?d(a):null;return b?(b.nativeSelection=c,b.docSelection=e,b.refresh(a)):(b=new l(c,e,a),a._rangySelection=b),b},a.getIframeSelection=function(b){return a.getSelection(r.getIframeWindow(b))},H=l.prototype,!z&&C&&s.areHostMethods(x,["removeAllRanges","addRange"])){H.removeAllRanges=function(){this.nativeSelection.removeAllRanges(),f(this)};var K=function(b,c){var d=t.getRangeDocument(c),d=a.createRange(d);d.collapseToPoint(c.endContainer,c.endOffset),b.nativeSelection.addRange(g(d)),b.nativeSelection.extend(c.startContainer,c.startOffset),b.refresh()};H.addRange=E?function(b,c){if(I&&y&&"Control"==this.docSelection.type)k(this,b);else if(c&&D)K(this,b);else{var d;F?d=this.rangeCount:(this.removeAllRanges(),d=0),this.nativeSelection.addRange(g(b)),this.rangeCount=this.nativeSelection.rangeCount,this.rangeCount==d+1?(a.config.checkSelectionRanges&&(d=J(this.nativeSelection,this.rangeCount-1))&&!t.rangesEqual(d,b)&&(b=new u(d)),this._ranges[this.rangeCount-1]=b,e(this,b,N(this.nativeSelection)),this.isCollapsed=q(this)):this.refresh()}}:function(a,b){b&&D?K(this,a):(this.nativeSelection.addRange(g(a)),this.refresh())},H.setRanges=function(a){if(I&&a.length>1)m(this,a);else{this.removeAllRanges();for(var b=0,c=a.length;c>b;++b)this.addRange(a[b])}}}else{if(!(s.isHostMethod(x,"empty")&&s.isHostMethod(A,"select")&&I&&z))return b.fail("No means of selecting a Range or TextRange was found"),!1;H.removeAllRanges=function(){try{if(this.docSelection.empty(),"None"!=this.docSelection.type){var a;if(this.anchorNode)a=r.getDocument(this.anchorNode);else if("Control"==this.docSelection.type){var b=this.docSelection.createRange();b.length&&(a=r.getDocument(b.item(0)).body.createTextRange())}a&&(a.body.createTextRange().select(),this.docSelection.empty())}}catch(c){}f(this)},H.addRange=function(a){"Control"==this.docSelection.type?k(this,a):(u.rangeToTextRange(a).select(),this._ranges[0]=a,this.rangeCount=1,this.isCollapsed=this._ranges[0].collapsed,e(this,a,!1))},H.setRanges=function(a){this.removeAllRanges();var b=a.length;b>1?m(this,a):b&&this.addRange(a[0])}}H.getRangeAt=function(a){if(0>a||a>=this.rangeCount)throw new v("INDEX_SIZE_ERR");return this._ranges[a]};var L;if(z)L=function(b){var c;a.isSelectionValid(b.win)?c=b.docSelection.createRange():(c=r.getBody(b.win.document).createTextRange(),c.collapse(!0)),"Control"==b.docSelection.type?j(b):c&&"undefined"!=typeof c.text?i(b,c):f(b)};else if(s.isHostMethod(x,"getRangeAt")&&"number"==typeof x.rangeCount)L=function(b){if(I&&y&&"Control"==b.docSelection.type)j(b);else if(b._ranges.length=b.rangeCount=b.nativeSelection.rangeCount,b.rangeCount){for(var c=0,d=b.rangeCount;d>c;++c)b._ranges[c]=new a.WrappedRange(b.nativeSelection.getRangeAt(c));e(b,b._ranges[b.rangeCount-1],N(b.nativeSelection)),b.isCollapsed=q(b)}else f(b)};else{if(!C||"boolean"!=typeof x.isCollapsed||"boolean"!=typeof A.collapsed||!a.features.implementsDomRange)return b.fail("No means of obtaining a Range or TextRange from the user's selection was found"),!1;L=function(a){var b;b=a.nativeSelection,b.anchorNode?(b=J(b,0),a._ranges=[b],a.rangeCount=1,b=a.nativeSelection,a.anchorNode=b.anchorNode,a.anchorOffset=b.anchorOffset,a.focusNode=b.focusNode,a.focusOffset=b.focusOffset,a.isCollapsed=q(a)):f(a)}}H.refresh=function(a){var b=a?this._ranges.slice(0):null;if(L(this),a){if(a=b.length,a!=this._ranges.length)return!1;for(;a--;)if(!t.rangesEqual(b[a],this._ranges[a]))return!1;return!0}};var M=function(a,b){var c=a.getAllRanges(),d=!1;a.removeAllRanges();for(var e=0,g=c.length;g>e;++e)d||b!==c[e]?a.addRange(c[e]):d=!0;a.rangeCount||f(a)};H.removeRange=I?function(a){if("Control"==this.docSelection.type){for(var b,c=this.docSelection.createRange(),a=h(a),d=r.getDocument(c.item(0)),d=r.getBody(d).createControlRange(),e=!1,f=0,g=c.length;g>f;++f)b=c.item(f),b!==a||e?d.add(c.item(f)):e=!0;d.select(),j(this)}else M(this,a)}:function(a){M(this,a)};var N;!z&&C&&a.features.implementsDomRange?(N=function(a){var b=!1;return a.anchorNode&&(b=1==r.comparePoints(a.anchorNode,a.anchorOffset,a.focusNode,a.focusOffset)),b},H.isBackwards=function(){return N(this)}):N=H.isBackwards=function(){return!1},H.toString=function(){for(var a=[],b=0,c=this.rangeCount;c>b;++b)a[b]=""+this._ranges[b];return a.join("")},H.collapse=function(b,c){n(this,b);var d=a.createRange(r.getDocument(b));d.collapseToPoint(b,c),this.removeAllRanges(),this.addRange(d),this.isCollapsed=!0},H.collapseToStart=function(){if(!this.rangeCount)throw new v("INVALID_STATE_ERR");var a=this._ranges[0];this.collapse(a.startContainer,a.startOffset)},H.collapseToEnd=function(){if(!this.rangeCount)throw new v("INVALID_STATE_ERR");var a=this._ranges[this.rangeCount-1];this.collapse(a.endContainer,a.endOffset)},H.selectAllChildren=function(b){n(this,b);var c=a.createRange(r.getDocument(b));c.selectNodeContents(b),this.removeAllRanges(),this.addRange(c)},H.deleteFromDocument=function(){if(I&&y&&"Control"==this.docSelection.type){for(var a,b=this.docSelection.createRange();b.length;)a=b.item(0),b.remove(a),a.parentNode.removeChild(a);this.refresh()}else if(this.rangeCount){b=this.getAllRanges(),this.removeAllRanges(),a=0;for(var c=b.length;c>a;++a)b[a].deleteContents();this.addRange(b[c-1])}},H.getAllRanges=function(){return this._ranges.slice(0)},H.setSingleRange=function(a){this.setRanges([a])},H.containsNode=function(a,b){for(var c=0,d=this._ranges.length;d>c;++c)if(this._ranges[c].containsNode(a,b))return!0;return!1},H.toHtml=function(){var a="";if(this.rangeCount){for(var a=t.getRangeDocument(this._ranges[0]).createElement("div"),b=0,c=this._ranges.length;c>b;++b)a.appendChild(this._ranges[b].cloneContents());a=a.innerHTML}return a},H.getName=function(){return"WrappedSelection"},H.inspect=function(){return o(this)},H.detach=function(){this.win=this.anchorNode=this.focusNode=this.win._rangySelection=null},l.inspect=o,a.Selection=l,a.selectionPrototype=H,a.addCreateMissingNativeApiListener(function(b){"undefined"==typeof b.getSelection&&(b.getSelection=function(){return a.getSelection(this)}),b=null})});var Base=function(){};Base.extend=function(a,b){var c=Base.prototype.extend;Base._prototyping=!0;var d=new this;c.call(d,a),d.base=function(){},delete Base._prototyping;var e=d.constructor,f=d.constructor=function(){if(!Base._prototyping)if(this._constructing||this.constructor==f)this._constructing=!0,e.apply(this,arguments),delete this._constructing;else if(null!=arguments[0])return(arguments[0].extend||c).call(arguments[0],d)};return f.ancestor=this,f.extend=this.extend,f.forEach=this.forEach,f.implement=this.implement,f.prototype=d,f.toString=this.toString,f.valueOf=function(a){return"object"==a?f:e.valueOf()},c.call(f,b),"function"==typeof f.init&&f.init(),f},Base.prototype={extend:function(a,b){if(1<arguments.length){var c=this[a];if(c&&"function"==typeof b&&(!c.valueOf||c.valueOf()!=b.valueOf())&&/\bbase\b/.test(b)){var d=b.valueOf(),b=function(){var a=this.base||Base.prototype.base;this.base=c;var b=d.apply(this,arguments);return this.base=a,b};b.valueOf=function(a){return"object"==a?b:d},b.toString=Base.toString}this[a]=b}else if(a){var e=Base.prototype.extend;!Base._prototyping&&"function"!=typeof this&&(e=this.extend||e);for(var f={toSource:null},g=["constructor","toString","valueOf"],h=Base._prototyping?0:1;i=g[h++];)a[i]!=f[i]&&e.call(this,i,a[i]);for(var i in a)f[i]||e.call(this,i,a[i])}return this}},Base=Base.extend({constructor:function(a){this.extend(a)}},{ancestor:Object,version:"1.1",forEach:function(a,b,c){for(var d in a)void 0===this.prototype[d]&&b.call(c,a[d],d,a)},implement:function(){for(var a=0;a<arguments.length;a++)"function"==typeof arguments[a]?arguments[a](this.prototype):this.prototype.extend(arguments[a]);return this},toString:function(){return""+this.valueOf()}}),wysihtml5.browser=function(){var a=navigator.userAgent,b=document.createElement("div"),c=-1!==a.indexOf("MSIE")&&-1===a.indexOf("Opera"),d=-1!==a.indexOf("Gecko")&&-1===a.indexOf("KHTML"),e=-1!==a.indexOf("AppleWebKit/"),f=-1!==a.indexOf("Chrome/"),g=-1!==a.indexOf("Opera/");return{USER_AGENT:a,supported:function(){var a=this.USER_AGENT.toLowerCase(),c="contentEditable"in b,d=document.execCommand&&document.queryCommandSupported&&document.queryCommandState,e=document.querySelector&&document.querySelectorAll,a=this.isIos()&&5>(/ipad|iphone|ipod/.test(a)&&a.match(/ os (\d+).+? like mac os x/)||[,0])[1]||-1!==a.indexOf("opera mobi")||-1!==a.indexOf("hpwos/");return c&&d&&e&&!a},isTouchDevice:function(){return this.supportsEvent("touchmove")},isIos:function(){var a=this.USER_AGENT.toLowerCase();return-1!==a.indexOf("webkit")&&-1!==a.indexOf("mobile")},supportsSandboxedIframes:function(){return c},throwsMixedContentWarningWhenIframeSrcIsEmpty:function(){return!("querySelector"in document)},displaysCaretInEmptyContentEditableCorrectly:function(){return!d},hasCurrentStyleProperty:function(){return"currentStyle"in b},insertsLineBreaksOnReturn:function(){return d},supportsPlaceholderAttributeOn:function(a){return"placeholder"in a},supportsEvent:function(a){var c;return(c="on"+a in b)||(b.setAttribute("on"+a,"return;"),c="function"==typeof b["on"+a]),c},supportsEventsInIframeCorrectly:function(){return!g},firesOnDropOnlyWhenOnDragOverIsCancelled:function(){return e||d},supportsDataTransfer:function(){try{return e&&(window.Clipboard||window.DataTransfer).prototype.getData}catch(a){return!1}},supportsHTML5Tags:function(a){return a=a.createElement("div"),a.innerHTML="<article>foo</article>","<article>foo</article>"===a.innerHTML.toLowerCase()},supportsCommand:function(){var a={formatBlock:c,insertUnorderedList:c||g||e,insertOrderedList:c||g||e},b={insertHTML:d};return function(c,d){if(!a[d]){try{return c.queryCommandSupported(d)}catch(e){}try{return c.queryCommandEnabled(d)}catch(f){return!!b[d]}}return!1}}(),doesAutoLinkingInContentEditable:function(){return c},canDisableAutoLinking:function(){return this.supportsCommand(document,"AutoUrlDetect")},clearsContentEditableCorrectly:function(){return d||g||e},supportsGetAttributeCorrectly:function(){return"1"!=document.createElement("td").getAttribute("rowspan")},canSelectImagesInContentEditable:function(){return d||c||g},clearsListsInContentEditableCorrectly:function(){return d||c||e},autoScrollsToCaret:function(){return!e},autoClosesUnclosedTags:function(){var a,c=b.cloneNode(!1);return c.innerHTML="<p><div></div>",c=c.innerHTML.toLowerCase(),a="<p></p><div></div>"===c||"<p><div></div></p>"===c,this.autoClosesUnclosedTags=function(){return a},a},supportsNativeGetElementsByClassName:function(){return-1!==(""+document.getElementsByClassName).indexOf("[native code]")},supportsSelectionModify:function(){return"getSelection"in window&&"modify"in window.getSelection()},supportsClassList:function(){return"classList"in b},needsSpaceAfterLineBreak:function(){return g},supportsSpeechApiOn:function(b){return 11<=(a.match(/Chrome\/(\d+)/)||[,0])[1]&&("onwebkitspeechchange"in b||"speech"in b)},crashesWhenDefineProperty:function(a){return c&&("XMLHttpRequest"===a||"XDomainRequest"===a)},doesAsyncFocus:function(){return c},hasProblemsSettingCaretAfterImg:function(){return c},hasUndoInContextMenu:function(){return d||f||g}}}(),wysihtml5.lang.array=function(a){return{contains:function(b){if(a.indexOf)return-1!==a.indexOf(b);for(var c=0,d=a.length;d>c;c++)if(a[c]===b)return!0;return!1},without:function(b){for(var b=wysihtml5.lang.array(b),c=[],d=0,e=a.length;e>d;d++)b.contains(a[d])||c.push(a[d]);return c},get:function(){for(var b=0,c=a.length,d=[];c>b;b++)d.push(a[b]);return d}}},wysihtml5.lang.Dispatcher=Base.extend({observe:function(a,b){return this.events=this.events||{},this.events[a]=this.events[a]||[],this.events[a].push(b),this},on:function(){return this.observe.apply(this,wysihtml5.lang.array(arguments).get())},fire:function(a,b){this.events=this.events||{};for(var c=this.events[a]||[],d=0;d<c.length;d++)c[d].call(this,b);return this},stopObserving:function(a,b){this.events=this.events||{};var c,d,e=0;if(a){for(c=this.events[a]||[],d=[];e<c.length;e++)c[e]!==b&&b&&d.push(c[e]);this.events[a]=d}else this.events={};return this}}),wysihtml5.lang.object=function(a){return{merge:function(b){for(var c in b)a[c]=b[c];return this},get:function(){return a},clone:function(){var b,c={};for(b in a)c[b]=a[b];return c},isArray:function(){return"[object Array]"===Object.prototype.toString.call(a)}}},function(){var a=/^\s+/,b=/\s+$/;wysihtml5.lang.string=function(c){return c=""+c,{trim:function(){return c.replace(a,"").replace(b,"")},interpolate:function(a){for(var b in a)c=this.replace("#{"+b+"}").by(a[b]);return c},replace:function(a){return{by:function(b){return c.split(a).join(b)}}}}}}(),function(a){function b(a){return a.replace(e,function(a,b){var c=(b.match(f)||[])[1]||"",d=h[c],b=b.replace(f,"");b.split(d).length>b.split(c).length&&(b+=c,c="");var e=d=b;return b.length>g&&(e=e.substr(0,g)+"..."),"www."===d.substr(0,4)&&(d="http://"+d),'<a href="'+d+'">'+e+"</a>"+c})}function c(f){if(!d.contains(f.nodeName)){if(f.nodeType!==a.TEXT_NODE||!f.data.match(e)){for(h=a.lang.array(f.childNodes).get(),g=h.length,i=0;g>i;i++)c(h[i]);return f}var g,h=f.parentNode;g=h.ownerDocument;var i=g._wysihtml5_tempElement;for(i||(i=g._wysihtml5_tempElement=g.createElement("div")),g=i,g.innerHTML="<span></span>"+b(f.data),g.removeChild(g.firstChild);g.firstChild;)h.insertBefore(g.firstChild,f);h.removeChild(f)}}var d=a.lang.array("CODE PRE A SCRIPT HEAD TITLE STYLE".split(" ")),e=/((https?:\/\/|www\.)[^\s<]{3,})/gi,f=/([^\w\/\-](,?))$/i,g=100,h={")":"(","]":"[","}":"{"};a.dom.autoLink=function(a){var b;a:{b=a;for(var e;b.parentNode;){if(b=b.parentNode,e=b.nodeName,d.contains(e)){b=!0;break a}if("body"===e)break}b=!1}return b?a:(a===a.ownerDocument.documentElement&&(a=a.ownerDocument.body),c(a))},a.dom.autoLink.URL_REG_EXP=e}(wysihtml5),function(a){var b=a.browser.supportsClassList(),c=a.dom;c.addClass=function(a,d){return b?a.classList.add(d):(c.hasClass(a,d)||(a.className+=" "+d),void 0)},c.removeClass=function(a,c){return b?a.classList.remove(c):(a.className=a.className.replace(RegExp("(^|\\s+)"+c+"(\\s+|$)")," "),void 0)},c.hasClass=function(a,c){if(b)return a.classList.contains(c);var d=a.className;return 0<d.length&&(d==c||RegExp("(^|\\s)"+c+"(\\s|$)").test(d))}}(wysihtml5),wysihtml5.dom.contains=function(){var a=document.documentElement;return a.contains?function(a,b){return b.nodeType!==wysihtml5.ELEMENT_NODE&&(b=b.parentNode),a!==b&&a.contains(b)}:a.compareDocumentPosition?function(a,b){return!!(16&a.compareDocumentPosition(b))}:void 0}(),wysihtml5.dom.convertToList=function(){function a(a,b){var c=a.createElement("li");return b.appendChild(c),c}return function(b,c){if("UL"===b.nodeName||"OL"===b.nodeName||"MENU"===b.nodeName)return b;var d,e,f,g,h,i=b.ownerDocument,j=i.createElement(c),k=b.querySelectorAll("br"),l=k.length;for(h=0;l>h;h++)for(d=k[h];(e=d.parentNode)&&e!==b&&e.lastChild===d;){if("block"===wysihtml5.dom.getStyle("display").from(e)){e.removeChild(d);break}wysihtml5.dom.insert(d).after(d.parentNode)}for(k=wysihtml5.lang.array(b.childNodes).get(),l=k.length,h=0;l>h;h++)g=g||a(i,j),d=k[h],e="block"===wysihtml5.dom.getStyle("display").from(d),f="BR"===d.nodeName,e?(g=g.firstChild?a(i,j):g,g.appendChild(d),g=null):f?g=g.firstChild?null:g:g.appendChild(d);return b.parentNode.replaceChild(j,b),j}}(),wysihtml5.dom.copyAttributes=function(a){return{from:function(b){return{to:function(c){for(var d,e=0,f=a.length;f>e;e++)d=a[e],"undefined"!=typeof b[d]&&""!==b[d]&&(c[d]=b[d]);return{andTo:arguments.callee}}}}}},function(a){var b=["-webkit-box-sizing","-moz-box-sizing","-ms-box-sizing","box-sizing"],c=function(c){var d;a:for(var e=0,f=b.length;f>e;e++)if("border-box"===a.getStyle(b[e]).from(c)){d=b[e];break a}return d?parseInt(a.getStyle("width").from(c),10)<c.offsetWidth:!1};a.copyStyles=function(d){return{from:function(e){c(e)&&(d=wysihtml5.lang.array(d).without(b));for(var f,g="",h=d.length,i=0;h>i;i++)f=d[i],g+=f+":"+a.getStyle(f).from(e)+";";return{to:function(b){return a.setStyles(g).on(b),{andTo:arguments.callee}}}}}}}(wysihtml5.dom),function(a){a.dom.delegate=function(b,c,d,e){return a.dom.observe(b,d,function(d){for(var f=d.target,g=a.lang.array(b.querySelectorAll(c));f&&f!==b;){if(g.contains(f)){e.call(f,d);break}f=f.parentNode}})}}(wysihtml5),wysihtml5.dom.getAsDom=function(){var a="abbr article aside audio bdi canvas command datalist details figcaption figure footer header hgroup keygen mark meter nav output progress rp rt ruby svg section source summary time track video wbr".split(" ");return function(b,c){var d,c=c||document;if("object"==typeof b&&b.nodeType)d=c.createElement("div"),d.appendChild(b);else if(wysihtml5.browser.supportsHTML5Tags(c))d=c.createElement("div"),d.innerHTML=b;else{if(d=c,!d._wysihtml5_supportsHTML5Tags){for(var e=0,f=a.length;f>e;e++)d.createElement(a[e]);d._wysihtml5_supportsHTML5Tags=!0}d=c,e=d.createElement("div"),e.style.display="none",d.body.appendChild(e);try{e.innerHTML=b}catch(g){}d.body.removeChild(e),d=e}return d}}(),wysihtml5.dom.getParentElement=function(){function a(a,b){return b&&b.length?"string"==typeof b?a===b:wysihtml5.lang.array(b).contains(a):!0}return function(b,c,d){if(d=d||50,c.className||c.classRegExp){a:{for(var e=c.nodeName,f=c.className,c=c.classRegExp;d--&&b&&"BODY"!==b.nodeName;){var g;if((g=b.nodeType===wysihtml5.ELEMENT_NODE)&&(g=a(b.nodeName,e))){g=f;var h=(b.className||"").match(c)||[];g=g?h[h.length-1]===g:!!h.length}if(g)break a;b=b.parentNode}b=null}return b}a:{for(e=c.nodeName,f=d;f--&&b&&"BODY"!==b.nodeName;){if(a(b.nodeName,e))break a;b=b.parentNode}b=null}return b}}(),wysihtml5.dom.getStyle=function(){function a(a){return a.replace(c,function(a){return a.charAt(1).toUpperCase()})}var b={"float":"styleFloat"in document.createElement("div").style?"styleFloat":"cssFloat"},c=/\-[a-z]/g;return function(c){return{from:function(d){if(d.nodeType===wysihtml5.ELEMENT_NODE){var e=d.ownerDocument,f=b[c]||a(c),g=d.style,h=d.currentStyle,i=g[f];if(i)return i;if(h)try{return h[f]}catch(j){}var k,f=e.defaultView||e.parentWindow,e=("height"===c||"width"===c)&&"TEXTAREA"===d.nodeName;if(f.getComputedStyle)return e&&(k=g.overflow,g.overflow="hidden"),d=f.getComputedStyle(d,null).getPropertyValue(c),e&&(g.overflow=k||""),d}}}}}(),wysihtml5.dom.hasElementWithTagName=function(){var a={},b=1;return function(c,d){var e=(c._wysihtml5_identifier||(c._wysihtml5_identifier=b++))+":"+d,f=a[e];return f||(f=a[e]=c.getElementsByTagName(d)),0<f.length}}(),function(a){var b={},c=1;a.dom.hasElementWithClassName=function(d,e){if(!a.browser.supportsNativeGetElementsByClassName())return!!d.querySelector("."+e);var f=(d._wysihtml5_identifier||(d._wysihtml5_identifier=c++))+":"+e,g=b[f];return g||(g=b[f]=d.getElementsByClassName(e)),0<g.length}}(wysihtml5),wysihtml5.dom.insert=function(a){return{after:function(b){b.parentNode.insertBefore(a,b.nextSibling)},before:function(b){b.parentNode.insertBefore(a,b)},into:function(b){b.appendChild(a)}}},wysihtml5.dom.insertCSS=function(a){return a=a.join("\n"),{into:function(b){var c=b.head||b.getElementsByTagName("head")[0],d=b.createElement("style");d.type="text/css",d.styleSheet?d.styleSheet.cssText=a:d.appendChild(b.createTextNode(a)),c&&c.appendChild(d)}}},wysihtml5.dom.observe=function(a,b,c){for(var d,e,b="string"==typeof b?[b]:b,f=0,g=b.length;g>f;f++)e=b[f],a.addEventListener?a.addEventListener(e,c,!1):(d=function(b){"target"in b||(b.target=b.srcElement),b.preventDefault=b.preventDefault||function(){this.returnValue=!1},b.stopPropagation=b.stopPropagation||function(){this.cancelBubble=!0},c.call(a,b)},a.attachEvent("on"+e,d));return{stop:function(){for(var e,f=0,g=b.length;g>f;f++)e=b[f],a.removeEventListener?a.removeEventListener(e,c,!1):a.detachEvent("on"+e,d)}}},wysihtml5.dom.parse=function(){function a(b,e){var f,g=b.childNodes,h=g.length;f=c[b.nodeType];var i=0;if(f=f&&f(b),!f)return null;for(i=0;h>i;i++)(newChild=a(g[i],e))&&f.appendChild(newChild);return e&&1>=f.childNodes.length&&f.nodeName.toLowerCase()===d&&!f.attributes.length?f.firstChild:f}function b(a,b){var c,b=b.toLowerCase();if((c="IMG"==a.nodeName)&&(c="src"==b)){var d;try{d=a.complete&&!a.mozMatchesSelector(":-moz-broken")}catch(e){a.complete&&"complete"===a.readyState&&(d=!0)}c=!0===d}return c?a.src:h&&"outerHTML"in a?-1!=a.outerHTML.toLowerCase().indexOf(" "+b+"=")?a.getAttribute(b):null:a.getAttribute(b)}var c={1:function(a){var c,f,h=g.tags;if(f=a.nodeName.toLowerCase(),c=a.scopeName,a._wysihtml5)return null;if(a._wysihtml5=1,"wysihtml5-temp"===a.className)return null;if(c&&"HTML"!=c&&(f=c+":"+f),"outerHTML"in a&&!wysihtml5.browser.autoClosesUnclosedTags()&&"P"===a.nodeName&&"</p>"!==a.outerHTML.slice(-4).toLowerCase()&&(f="div"),f in h){if(c=h[f],!c||c.remove)return null;c="string"==typeof c?{rename_tag:c}:c}else{if(!a.firstChild)return null;c={rename_tag:d}}f=a.ownerDocument.createElement(c.rename_tag||f);var h={},k=c.set_class,l=c.add_class,m=c.set_attributes,n=c.check_attributes,o=g.classes,p=0,q=[];c=[];var r,s=[],t=[];if(m&&(h=wysihtml5.lang.object(m).clone()),n)for(r in n)(m=i[n[r]])&&(m=m(b(a,r)),"string"==typeof m&&(h[r]=m));if(k&&q.push(k),l)for(r in l)(m=j[l[r]])&&(k=m(b(a,r)),"string"==typeof k&&q.push(k));for(o["_wysihtml5-temp-placeholder"]=1,(t=a.getAttribute("class"))&&(q=q.concat(t.split(e))),l=q.length;l>p;p++)a=q[p],o[a]&&c.push(a);for(o=c.length;o--;)a=c[o],wysihtml5.lang.array(s).contains(a)||s.unshift(a);s.length&&(h["class"]=s.join(" "));for(r in h)try{f.setAttribute(r,h[r])}catch(u){}return h.src&&("undefined"!=typeof h.width&&f.setAttribute("width",h.width),"undefined"!=typeof h.height&&f.setAttribute("height",h.height)),f},3:function(a){return a.ownerDocument.createTextNode(a.data)}},d="span",e=/\s+/,f={tags:{},classes:{}},g={},h=!wysihtml5.browser.supportsGetAttributeCorrectly(),i={url:function(){var a=/^https?:\/\//i;return function(b){return b&&b.match(a)?b.replace(a,function(a){return a.toLowerCase()}):null}}(),alt:function(){var a=/[^ a-z0-9_\-]/gi;return function(b){return b?b.replace(a,""):""}}(),numbers:function(){var a=/\D/g;return function(b){return(b=(b||"").replace(a,""))||null}}()},j={align_img:function(){var a={left:"wysiwyg-float-left",right:"wysiwyg-float-right"};return function(b){return a[(""+b).toLowerCase()]}}(),align_text:function(){var a={left:"wysiwyg-text-align-left",right:"wysiwyg-text-align-right",center:"wysiwyg-text-align-center",justify:"wysiwyg-text-align-justify"};return function(b){return a[(""+b).toLowerCase()]}}(),clear_br:function(){var a={left:"wysiwyg-clear-left",right:"wysiwyg-clear-right",both:"wysiwyg-clear-both",all:"wysiwyg-clear-both"};return function(b){return a[(""+b).toLowerCase()]}}(),size_font:function(){var a={1:"wysiwyg-font-size-xx-small",2:"wysiwyg-font-size-small",3:"wysiwyg-font-size-medium",4:"wysiwyg-font-size-large",5:"wysiwyg-font-size-x-large",6:"wysiwyg-font-size-xx-large",7:"wysiwyg-font-size-xx-large","-":"wysiwyg-font-size-smaller","+":"wysiwyg-font-size-larger"};return function(b){return a[(""+b).charAt(0)]}}()};return function(b,c,d,e){wysihtml5.lang.object(g).merge(f).merge(c).get();for(var d=d||b.ownerDocument||document,c=d.createDocumentFragment(),h="string"==typeof b,b=h?wysihtml5.dom.getAsDom(b,d):b;b.firstChild;)d=b.firstChild,b.removeChild(d),(d=a(d,e))&&c.appendChild(d);return b.innerHTML="",b.appendChild(c),h?wysihtml5.quirks.getCorrectInnerHTML(b):b}}(),wysihtml5.dom.removeEmptyTextNodes=function(a){for(var b=wysihtml5.lang.array(a.childNodes).get(),c=b.length,d=0;c>d;d++)a=b[d],a.nodeType===wysihtml5.TEXT_NODE&&""===a.data&&a.parentNode.removeChild(a)},wysihtml5.dom.renameElement=function(a,b){for(var c,d=a.ownerDocument.createElement(b);c=a.firstChild;)d.appendChild(c);return wysihtml5.dom.copyAttributes(["align","className"]).from(a).to(d),a.parentNode.replaceChild(d,a),d},wysihtml5.dom.replaceWithChildNodes=function(a){if(a.parentNode)if(a.firstChild){for(var b=a.ownerDocument.createDocumentFragment();a.firstChild;)b.appendChild(a.firstChild);a.parentNode.replaceChild(b,a)}else a.parentNode.removeChild(a)},function(a){function b(a){var b=a.ownerDocument.createElement("br");a.appendChild(b)}a.resolveList=function(c){if("MENU"===c.nodeName||"UL"===c.nodeName||"OL"===c.nodeName){var d,e,f,g=c.ownerDocument.createDocumentFragment(),h=c.previousElementSibling||c.previousSibling;for(h&&"block"!==a.getStyle("display").from(h)&&b(g);f=c.firstChild;){for(d=f.lastChild;h=f.firstChild;)e=(e=h===d)&&"block"!==a.getStyle("display").from(h)&&"BR"!==h.nodeName,g.appendChild(h),e&&b(g);f.parentNode.removeChild(f)}c.parentNode.replaceChild(g,c)}}}(wysihtml5.dom),function(a){var b=document,c="parent top opener frameElement frames localStorage globalStorage sessionStorage indexedDB".split(" "),d="open close openDialog showModalDialog alert confirm prompt openDatabase postMessage XMLHttpRequest XDomainRequest".split(" "),e=["referrer","write","open","close"];a.dom.Sandbox=Base.extend({constructor:function(b,c){this.callback=b||a.EMPTY_FUNCTION,this.config=a.lang.object({}).merge(c).get(),this.iframe=this._createIframe()},insertInto:function(a){"string"==typeof a&&(a=b.getElementById(a)),a.appendChild(this.iframe)},getIframe:function(){return this.iframe},getWindow:function(){this._readyError()},getDocument:function(){this._readyError()},destroy:function(){var a=this.getIframe();a.parentNode.removeChild(a)},_readyError:function(){throw Error("wysihtml5.Sandbox: Sandbox iframe isn't loaded yet")},_createIframe:function(){var c=this,d=b.createElement("iframe");return d.className="wysihtml5-sandbox",a.dom.setAttributes({security:"restricted",allowtransparency:"true",frameborder:0,width:0,height:0,marginwidth:0,marginheight:0}).on(d),a.browser.throwsMixedContentWarningWhenIframeSrcIsEmpty()&&(d.src="javascript:'<html></html>'"),d.onload=function(){d.onreadystatechange=d.onload=null,c._onLoadIframe(d)},d.onreadystatechange=function(){/loaded|complete/.test(d.readyState)&&(d.onreadystatechange=d.onload=null,c._onLoadIframe(d))},d},_onLoadIframe:function(f){if(a.dom.contains(b.documentElement,f)){var g=this,h=f.contentWindow,i=f.contentWindow.document,j=this._getHtml({charset:b.characterSet||b.charset||"utf-8",stylesheets:this.config.stylesheets});if(i.open("text/html","replace"),i.write(j),i.close(),this.getWindow=function(){return f.contentWindow},this.getDocument=function(){return f.contentWindow.document},h.onerror=function(a,b,c){throw Error("wysihtml5.Sandbox: "+a,b,c)},!a.browser.supportsSandboxedIframes()){var k,j=0;for(k=c.length;k>j;j++)this._unset(h,c[j]);for(j=0,k=d.length;k>j;j++)this._unset(h,d[j],a.EMPTY_FUNCTION);for(j=0,k=e.length;k>j;j++)this._unset(i,e[j]);this._unset(i,"cookie","",!0)}this.loaded=!0,setTimeout(function(){g.callback(g)},0)}},_getHtml:function(b){var c,d=b.stylesheets,e="",f=0;if(d="string"==typeof d?[d]:d)for(c=d.length;c>f;f++)e+='<link rel="stylesheet" href="'+d[f]+'">';return b.stylesheets=e,a.lang.string('<!DOCTYPE html><html><head><meta charset="#{charset}">#{stylesheets}</head><body></body></html>').interpolate(b)},_unset:function(b,c,d,e){try{b[c]=d}catch(f){}try{b.__defineGetter__(c,function(){return d})}catch(g){}if(e)try{b.__defineSetter__(c,function(){})}catch(h){}if(!a.browser.crashesWhenDefineProperty(c))try{var i={get:function(){return d}};e&&(i.set=function(){}),Object.defineProperty(b,c,i)}catch(j){}}})}(wysihtml5),function(){var a={className:"class"};wysihtml5.dom.setAttributes=function(b){return{on:function(c){for(var d in b)c.setAttribute(a[d]||d,b[d])}}}}(),wysihtml5.dom.setStyles=function(a){return{on:function(b){if(b=b.style,"string"==typeof a)b.cssText+=";"+a;else for(var c in a)"float"===c?(b.cssFloat=a[c],b.styleFloat=a[c]):b[c]=a[c]}}},function(a){a.simulatePlaceholder=function(b,c,d){var e=function(){c.hasPlaceholderSet()&&c.clear(),a.removeClass(c.element,"placeholder")},f=function(){c.isEmpty()&&(c.setValue(d),a.addClass(c.element,"placeholder"))};b.observe("set_placeholder",f).observe("unset_placeholder",e).observe("focus:composer",e).observe("paste:composer",e).observe("blur:composer",f),f()}}(wysihtml5.dom),function(a){var b=document.documentElement;"textContent"in b?(a.setTextContent=function(a,b){a.textContent=b},a.getTextContent=function(a){return a.textContent}):"innerText"in b?(a.setTextContent=function(a,b){a.innerText=b},a.getTextContent=function(a){return a.innerText}):(a.setTextContent=function(a,b){a.nodeValue=b},a.getTextContent=function(a){return a.nodeValue})}(wysihtml5.dom),wysihtml5.quirks.cleanPastedHTML=function(){var a={"a u":wysihtml5.dom.replaceWithChildNodes};return function(b,c,d){var e,f,g,c=c||a,d=d||b.ownerDocument||document,h="string"==typeof b,i=0,b=h?wysihtml5.dom.getAsDom(b,d):b;for(g in c)for(e=b.querySelectorAll(g),d=c[g],f=e.length;f>i;i++)d(e[i]);return h?b.innerHTML:b}}(),function(a){var b=a.dom;a.quirks.ensureProperClearing=function(){var a=function(){var a=this;setTimeout(function(){var b=a.innerHTML.toLowerCase();("<p>&nbsp;</p>"==b||"<p>&nbsp;</p><p>&nbsp;</p>"==b)&&(a.innerHTML="")
},0)};return function(c){b.observe(c.element,["cut","keydown"],a)}}(),a.quirks.ensureProperClearingOfLists=function(){var c=["OL","UL","MENU"];return function(d){b.observe(d.element,"keydown",function(e){if(e.keyCode===a.BACKSPACE_KEY){var f=d.selection.getSelectedNode(),e=d.element;e.firstChild&&a.lang.array(c).contains(e.firstChild.nodeName)&&(f=b.getParentElement(f,{nodeName:c}))&&f==e.firstChild&&1>=f.childNodes.length&&(f.firstChild?""===f.firstChild.innerHTML:1)&&f.parentNode.removeChild(f)}})}}()}(wysihtml5),function(a){a.quirks.getCorrectInnerHTML=function(b){var c=b.innerHTML;if(-1===c.indexOf("%7E"))return c;var d,e,f,g,b=b.querySelectorAll("[href*='~'], [src*='~']");for(g=0,f=b.length;f>g;g++)d=b[g].href||b[g].src,e=a.lang.string(d).replace("~").by("%7E"),c=a.lang.string(c).replace(e).by(d);return c}}(wysihtml5),function(a){var b=a.dom,c="LI P H1 H2 H3 H4 H5 H6".split(" "),d=["UL","OL","MENU"];a.quirks.insertLineBreakOnReturn=function(e){function f(c){if(c=b.getParentElement(c,{nodeName:["P","DIV"]},2)){var d=document.createTextNode(a.INVISIBLE_SPACE);b.insert(d).before(c),b.replaceWithChildNodes(c),e.selection.selectNode(d)}}b.observe(e.element.ownerDocument,"keydown",function(g){var h=g.keyCode;if(!(g.shiftKey||h!==a.ENTER_KEY&&h!==a.BACKSPACE_KEY)){var i=e.selection.getSelectedNode();(i=b.getParentElement(i,{nodeName:c},4))?"LI"!==i.nodeName||h!==a.ENTER_KEY&&h!==a.BACKSPACE_KEY?i.nodeName.match(/H[1-6]/)&&h===a.ENTER_KEY&&setTimeout(function(){f(e.selection.getSelectedNode())},0):setTimeout(function(){var a,c=e.selection.getSelectedNode();c&&((a=b.getParentElement(c,{nodeName:d},2))||f(c))},0):h===a.ENTER_KEY&&!a.browser.insertsLineBreaksOnReturn()&&(e.commands.exec("insertLineBreak"),g.preventDefault())}})}}(wysihtml5),function(a){a.quirks.redraw=function(b){a.dom.addClass(b,"wysihtml5-quirks-redraw"),a.dom.removeClass(b,"wysihtml5-quirks-redraw");try{var c=b.ownerDocument;c.execCommand("italic",!1,null),c.execCommand("italic",!1,null)}catch(d){}}}(wysihtml5),function(a){var b=a.dom;a.Selection=Base.extend({constructor:function(a){window.rangy.init(),this.editor=a,this.composer=a.composer,this.doc=this.composer.doc},getBookmark:function(){var a=this.getRange();return a&&a.cloneRange()},setBookmark:function(a){a&&this.setSelection(a)},setBefore:function(a){var b=rangy.createRange(this.doc);return b.setStartBefore(a),b.setEndBefore(a),this.setSelection(b)},setAfter:function(a){var b=rangy.createRange(this.doc);return b.setStartAfter(a),b.setEndAfter(a),this.setSelection(b)},selectNode:function(c){var d=rangy.createRange(this.doc),e=c.nodeType===a.ELEMENT_NODE,f="canHaveHTML"in c?c.canHaveHTML:"IMG"!==c.nodeName,g=e?c.innerHTML:c.data,g=""===g||g===a.INVISIBLE_SPACE,h=b.getStyle("display").from(c),h="block"===h||"list-item"===h;if(g&&e&&f)try{c.innerHTML=a.INVISIBLE_SPACE}catch(i){}f?d.selectNodeContents(c):d.selectNode(c),f&&g&&e?d.collapse(h):f&&g&&(d.setStartAfter(c),d.setEndAfter(c)),this.setSelection(d)},getSelectedNode:function(a){return a&&this.doc.selection&&"Control"===this.doc.selection.type&&(a=this.doc.selection.createRange())&&a.length?a.item(0):(a=this.getSelection(this.doc),a.focusNode===a.anchorNode?a.focusNode:(a=this.getRange(this.doc))?a.commonAncestorContainer:this.doc.body)},executeAndRestore:function(b,c){var d=this.doc.body,e=c&&d.scrollTop,f=c&&d.scrollLeft,g='<span class="_wysihtml5-temp-placeholder">'+a.INVISIBLE_SPACE+"</span>",h=this.getRange(this.doc);if(h){g=h.createContextualFragment(g),h.insertNode(g);try{b(h.startContainer,h.endContainer)}catch(i){setTimeout(function(){throw i},0)}(caretPlaceholder=this.doc.querySelector("._wysihtml5-temp-placeholder"))?(h=rangy.createRange(this.doc),h.selectNode(caretPlaceholder),h.deleteContents(),this.setSelection(h)):d.focus(),c&&(d.scrollTop=e,d.scrollLeft=f);try{caretPlaceholder.parentNode.removeChild(caretPlaceholder)}catch(j){}}else b(d,d)},executeAndRestoreSimple:function(a){var b,c,d,e=this.getRange(),f=this.doc.body;if(e){b=e.getNodes([3]),f=b[0]||e.startContainer,d=b[b.length-1]||e.endContainer,b=f===e.startContainer?e.startOffset:0,c=d===e.endContainer?e.endOffset:d.length;try{a(e.startContainer,e.endContainer)}catch(g){setTimeout(function(){throw g},0)}a=rangy.createRange(this.doc);try{a.setStart(f,b)}catch(h){}try{a.setEnd(d,c)}catch(i){}try{this.setSelection(a)}catch(j){}}else a(f,f)},insertHTML:function(a){var a=rangy.createRange(this.doc).createContextualFragment(a),b=a.lastChild;this.insertNode(a),b&&this.setAfter(b)},insertNode:function(a){var b=this.getRange();b&&b.insertNode(a)},surround:function(a){var b=this.getRange();if(b)try{b.surroundContents(a),this.selectNode(a)}catch(c){a.appendChild(b.extractContents()),b.insertNode(a)}},scrollIntoView:function(){var b,c=this.doc,d=c.documentElement.scrollHeight>c.documentElement.offsetHeight;if((b=c._wysihtml5ScrollIntoViewElement)||(b=c.createElement("span"),b.innerHTML=a.INVISIBLE_SPACE),b=c._wysihtml5ScrollIntoViewElement=b,d){this.insertNode(b);var d=b,e=0;if(d.parentNode)do e+=d.offsetTop||0,d=d.offsetParent;while(d);d=e,b.parentNode.removeChild(b),d>c.body.scrollTop&&(c.body.scrollTop=d)}},selectLine:function(){a.browser.supportsSelectionModify()?this._selectLine_W3C():this.doc.selection&&this._selectLine_MSIE()},_selectLine_W3C:function(){var a=this.doc.defaultView.getSelection();a.modify("extend","left","lineboundary"),a.modify("extend","right","lineboundary")},_selectLine_MSIE:function(){var a,b=this.doc.selection.createRange(),c=b.boundingTop,d=this.doc.body.scrollWidth;if(b.moveToPoint){for(0===c&&(a=this.doc.createElement("span"),this.insertNode(a),c=a.offsetTop,a.parentNode.removeChild(a)),c+=1,a=-10;d>a;a+=2)try{b.moveToPoint(a,c);break}catch(e){}for(a=this.doc.selection.createRange();d>=0;d--)try{a.moveToPoint(d,c);break}catch(f){}b.setEndPoint("EndToEnd",a),b.select()}},getText:function(){var a=this.getSelection();return a?a.toString():""},getNodes:function(a,b){var c=this.getRange();return c?c.getNodes([a],b):[]},getRange:function(){var a=this.getSelection();return a&&a.rangeCount&&a.getRangeAt(0)},getSelection:function(){return rangy.getSelection(this.doc.defaultView||this.doc.parentWindow)},setSelection:function(a){return rangy.getSelection(this.doc.defaultView||this.doc.parentWindow).setSingleRange(a)}})}(wysihtml5),function(a,b){function c(a,c){return b.dom.isCharacterDataNode(a)?0==c?!!a.previousSibling:c==a.length?!!a.nextSibling:!0:c>0&&c<a.childNodes.length}function d(a,c,e){var f;if(b.dom.isCharacterDataNode(c)&&(0==e?(e=b.dom.getNodeIndex(c),c=c.parentNode):e==c.length?(e=b.dom.getNodeIndex(c)+1,c=c.parentNode):f=b.dom.splitDataNode(c,e)),!f){f=c.cloneNode(!1),f.id&&f.removeAttribute("id");for(var g;g=c.childNodes[e];)f.appendChild(g);b.dom.insertAfter(f,c)}return c==a?f:d(a,f.parentNode,b.dom.getNodeIndex(f))}function e(b){this.firstTextNode=(this.isElementMerge=b.nodeType==a.ELEMENT_NODE)?b.lastChild:b,this.textNodes=[this.firstTextNode]}function f(a,b,c,d){this.tagNames=a||[g],this.cssClass=b||"",this.similarClassRegExp=c,this.normalize=d,this.applyToAnyTagName=!1}var g="span",h=/\s+/g;e.prototype={doMerge:function(){for(var a,b,c=[],d=0,e=this.textNodes.length;e>d;++d)a=this.textNodes[d],b=a.parentNode,c[d]=a.data,d&&(b.removeChild(a),b.hasChildNodes()||b.parentNode.removeChild(b));return this.firstTextNode.data=c=c.join("")},getLength:function(){for(var a=this.textNodes.length,b=0;a--;)b+=this.textNodes[a].length;return b},toString:function(){for(var a=[],b=0,c=this.textNodes.length;c>b;++b)a[b]="'"+this.textNodes[b].data+"'";return"[Merge("+a.join(",")+")]"}},f.prototype={getAncestorWithClass:function(c){for(var d;c;){if(this.cssClass)if(d=this.cssClass,c.className){var e=c.className.match(this.similarClassRegExp)||[];d=e[e.length-1]===d}else d=!1;else d=!0;if(c.nodeType==a.ELEMENT_NODE&&b.dom.arrayContains(this.tagNames,c.tagName.toLowerCase())&&d)return c;c=c.parentNode}return!1},postApply:function(a,b){for(var c,d,f,g=a[0],h=a[a.length-1],i=[],j=g,k=h,l=0,m=h.length,n=0,o=a.length;o>n;++n)d=a[n],(f=this.getAdjacentMergeableTextNode(d.parentNode,!1))?(c||(c=new e(f),i.push(c)),c.textNodes.push(d),d===g&&(j=c.firstTextNode,l=j.length),d===h&&(k=c.firstTextNode,m=c.getLength())):c=null;if((g=this.getAdjacentMergeableTextNode(h.parentNode,!0))&&(c||(c=new e(h),i.push(c)),c.textNodes.push(g)),i.length){for(n=0,o=i.length;o>n;++n)i[n].doMerge();b.setStart(j,l),b.setEnd(k,m)}},getAdjacentMergeableTextNode:function(b,c){var d=b.nodeType==a.TEXT_NODE,e=d?b.parentNode:b,f=c?"nextSibling":"previousSibling";if(d){if((d=b[f])&&d.nodeType==a.TEXT_NODE)return d}else if((d=e[f])&&this.areElementsMergeable(b,d))return d[c?"firstChild":"lastChild"];return null},areElementsMergeable:function(a,c){var d;if((d=b.dom.arrayContains(this.tagNames,(a.tagName||"").toLowerCase()))&&(d=b.dom.arrayContains(this.tagNames,(c.tagName||"").toLowerCase()))&&(d=a.className.replace(h," ")==c.className.replace(h," ")))a:if(a.attributes.length!=c.attributes.length)d=!1;else{d=0;for(var e,f,g=a.attributes.length;g>d;++d)if(e=a.attributes[d],f=e.name,"class"!=f&&(f=c.attributes.getNamedItem(f),e.specified!=f.specified||e.specified&&e.nodeValue!==f.nodeValue)){d=!1;break a}d=!0}return d},createContainer:function(a){return a=a.createElement(this.tagNames[0]),this.cssClass&&(a.className=this.cssClass),a},applyToTextNode:function(a){var c=a.parentNode;1==c.childNodes.length&&b.dom.arrayContains(this.tagNames,c.tagName.toLowerCase())?this.cssClass&&(a=this.cssClass,c.className?(c.className&&(c.className=c.className.replace(this.similarClassRegExp,"")),c.className+=" "+a):c.className=a):(c=this.createContainer(b.dom.getDocument(a)),a.parentNode.insertBefore(c,a),c.appendChild(a))},isRemovable:function(c){return b.dom.arrayContains(this.tagNames,c.tagName.toLowerCase())&&a.lang.string(c.className).trim()==this.cssClass},undoToTextNode:function(a,b,e){if(b.containsNode(e)||(a=b.cloneRange(),a.selectNode(e),a.isPointInRange(b.endContainer,b.endOffset)&&c(b.endContainer,b.endOffset)&&(d(e,b.endContainer,b.endOffset),b.setEndAfter(e)),a.isPointInRange(b.startContainer,b.startOffset)&&c(b.startContainer,b.startOffset)&&(e=d(e,b.startContainer,b.startOffset))),this.similarClassRegExp&&e.className&&(e.className=e.className.replace(this.similarClassRegExp,"")),this.isRemovable(e)){for(b=e,e=b.parentNode;b.firstChild;)e.insertBefore(b.firstChild,b);e.removeChild(b)}},applyToRange:function(b){var c=b.getNodes([a.TEXT_NODE]);if(!c.length)try{var d=this.createContainer(b.endContainer.ownerDocument);return b.surroundContents(d),this.selectNode(b,d),void 0}catch(e){}if(b.splitBoundaries(),c=b.getNodes([a.TEXT_NODE]),c.length){for(var f=0,g=c.length;g>f;++f)d=c[f],this.getAncestorWithClass(d)||this.applyToTextNode(d);b.setStart(c[0],0),d=c[c.length-1],b.setEnd(d,d.length),this.normalize&&this.postApply(c,b)}},undoToRange:function(b){var c,d,e=b.getNodes([a.TEXT_NODE]);e.length?(b.splitBoundaries(),e=b.getNodes([a.TEXT_NODE])):(e=b.endContainer.ownerDocument.createTextNode(a.INVISIBLE_SPACE),b.insertNode(e),b.selectNode(e),e=[e]);for(var f=0,g=e.length;g>f;++f)c=e[f],(d=this.getAncestorWithClass(c))&&this.undoToTextNode(c,b,d);1==g?this.selectNode(b,e[0]):(b.setStart(e[0],0),c=e[e.length-1],b.setEnd(c,c.length),this.normalize&&this.postApply(e,b))},selectNode:function(b,c){var d=c.nodeType===a.ELEMENT_NODE,e="canHaveHTML"in c?c.canHaveHTML:!0,f=d?c.innerHTML:c.data;if((f=""===f||f===a.INVISIBLE_SPACE)&&d&&e)try{c.innerHTML=a.INVISIBLE_SPACE}catch(g){}b.selectNodeContents(c),f&&d?b.collapse(!1):f&&(b.setStartAfter(c),b.setEndAfter(c))},getTextSelectedByRange:function(a,b){var c=b.cloneRange();c.selectNodeContents(a);var d=c.intersection(b),d=d?d.toString():"";return c.detach(),d},isAppliedToRange:function(b){var c,d=[],e=b.getNodes([a.TEXT_NODE]);if(!e.length)return(c=this.getAncestorWithClass(b.startContainer))?[c]:!1;for(var f,g=0,h=e.length;h>g;++g){if(f=this.getTextSelectedByRange(e[g],b),c=this.getAncestorWithClass(e[g]),""!=f&&!c)return!1;d.push(c)}return d},toggleRange:function(a){this.isAppliedToRange(a)?this.undoToRange(a):this.applyToRange(a)}},a.selection.HTMLApplier=f}(wysihtml5,rangy),wysihtml5.Commands=Base.extend({constructor:function(a){this.editor=a,this.composer=a.composer,this.doc=this.composer.doc},support:function(a){return wysihtml5.browser.supportsCommand(this.doc,a)},exec:function(a,b){var c=wysihtml5.commands[a],d=wysihtml5.lang.array(arguments).get(),e=c&&c.exec,f=null;if(this.editor.fire("beforecommand:composer"),e)d.unshift(this.composer),f=e.apply(c,d);else try{f=this.doc.execCommand(a,!1,b)}catch(g){}return this.editor.fire("aftercommand:composer"),f},state:function(a){var b=wysihtml5.commands[a],c=wysihtml5.lang.array(arguments).get(),d=b&&b.state;if(d)return c.unshift(this.composer),d.apply(b,c);try{return this.doc.queryCommandState(a)}catch(e){return!1}},value:function(a){var b=wysihtml5.commands[a],c=b&&b.value;if(c)return c.call(b,this.composer,a);try{return this.doc.queryCommandValue(a)}catch(d){return null}}}),function(a){a.commands.bold={exec:function(b,c){return a.commands.formatInline.exec(b,c,"b")},state:function(b,c){return a.commands.formatInline.state(b,c,"b")},value:function(){}}}(wysihtml5),function(a){function b(b,f){var g,h,i,j=b.doc,k="_wysihtml5-temp-"+ +new Date,l=0;for(a.commands.formatInline.exec(b,c,d,k,/non-matching-class/g),g=j.querySelectorAll(d+"."+k),k=g.length;k>l;l++)for(i in h=g[l],h.removeAttribute("class"),f)h.setAttribute(i,f[i]);l=h,1===k&&(i=e.getTextContent(h),k=!!h.querySelector("*"),i=""===i||i===a.INVISIBLE_SPACE,!k&&i&&(e.setTextContent(h,f.text||h.href),j=j.createTextNode(" "),b.selection.setAfter(h),b.selection.insertNode(j),l=j)),b.selection.setAfter(l)}var c,d="A",e=a.dom;a.commands.createLink={exec:function(a,c,d){var f=this.state(a,c);f?a.selection.executeAndRestore(function(){for(var a,b,c,d=f.length,g=0;d>g;g++)a=f[g],b=e.getParentElement(a,{nodeName:"code"}),c=e.getTextContent(a),c.match(e.autoLink.URL_REG_EXP)&&!b?e.renameElement(a,"code"):e.replaceWithChildNodes(a)}):(d="object"==typeof d?d:{href:d},b(a,d))},state:function(b,c){return a.commands.formatInline.state(b,c,"A")},value:function(){return c}}}(wysihtml5),function(a){var b=/wysiwyg-font-size-[a-z\-]+/g;a.commands.fontSize={exec:function(c,d,e){return a.commands.formatInline.exec(c,d,"span","wysiwyg-font-size-"+e,b)},state:function(c,d,e){return a.commands.formatInline.state(c,d,"span","wysiwyg-font-size-"+e,b)},value:function(){}}}(wysihtml5),function(a){var b=/wysiwyg-color-[a-z]+/g;a.commands.foreColor={exec:function(c,d,e){return a.commands.formatInline.exec(c,d,"span","wysiwyg-color-"+e,b)},state:function(c,d,e){return a.commands.formatInline.state(c,d,"span","wysiwyg-color-"+e,b)},value:function(){}}}(wysihtml5),function(a){function b(b){for(b=b.previousSibling;b&&b.nodeType===a.TEXT_NODE&&!a.lang.string(b.data).trim();)b=b.previousSibling;return b}function c(b){for(b=b.nextSibling;b&&b.nodeType===a.TEXT_NODE&&!a.lang.string(b.data).trim();)b=b.nextSibling;return b}function d(a){return"BR"===a.nodeName||"block"===g.getStyle("display").from(a)?!0:!1}function e(b,c,d,e){if(e)var f=g.observe(b,"DOMNodeInserted",function(b){var c,b=b.target;b.nodeType===a.ELEMENT_NODE&&(c=g.getStyle("display").from(b),"inline"!==c.substr(0,6)&&(b.className+=" "+e))});b.execCommand(c,!1,d),f&&f.stop()}function f(a,d){a.selection.selectLine(),a.selection.surround(d);var e=c(d),f=b(d);e&&"BR"===e.nodeName&&e.parentNode.removeChild(e),f&&"BR"===f.nodeName&&f.parentNode.removeChild(f),(e=d.lastChild)&&"BR"===e.nodeName&&e.parentNode.removeChild(e),a.selection.selectNode(d)}var g=a.dom,h="H1 H2 H3 H4 H5 H6 P BLOCKQUOTE DIV".split(" ");a.commands.formatBlock={exec:function(i,j,k,l,m){var n,o=i.doc,p=this.state(i,j,k,l,m),k="string"==typeof k?k.toUpperCase():k;if(p)i.selection.executeAndRestoreSimple(function(){m&&(p.className=p.className.replace(m,""));var e=!!a.lang.string(p.className).trim();if(e||p.nodeName!==(k||"DIV"))e&&g.renameElement(p,"DIV");else{var e=p,f=e.ownerDocument,h=c(e),i=b(e);h&&!d(h)&&e.parentNode.insertBefore(f.createElement("br"),h),i&&!d(i)&&e.parentNode.insertBefore(f.createElement("br"),e),g.replaceWithChildNodes(p)}});else{if((null===k||a.lang.array(h).contains(k))&&(n=i.selection.getSelectedNode(),p=g.getParentElement(n,{nodeName:h})))return i.selection.executeAndRestoreSimple(function(){if(k&&(p=g.renameElement(p,k)),l){var a=p;a.className?(a.className=a.className.replace(m,""),a.className+=" "+l):a.className=l}}),void 0;i.commands.support(j)?e(o,j,k||"DIV",l):(p=o.createElement(k||"DIV"),l&&(p.className=l),f(i,p))}},state:function(a,b,c,d,e){return c="string"==typeof c?c.toUpperCase():c,a=a.selection.getSelectedNode(),g.getParentElement(a,{nodeName:c,className:d,classRegExp:e})},value:function(){}}}(wysihtml5),function(a){function b(b,e,f){var g=b+":"+e;if(!d[g]){var h=d,i=a.selection.HTMLApplier,j=c[b],b=j?[b.toLowerCase(),j.toLowerCase()]:[b.toLowerCase()];h[g]=new i(b,e,f,!0)}return d[g]}var c={strong:"b",em:"i",b:"strong",i:"em"},d={};a.commands.formatInline={exec:function(a,c,d,e,f){return(c=a.selection.getRange())?(b(d,e,f).toggleRange(c),a.selection.setSelection(c),void 0):!1},state:function(d,e,f,g,h){var e=d.doc,i=c[f]||f;return!a.dom.hasElementWithTagName(e,f)&&!a.dom.hasElementWithTagName(e,i)||g&&!a.dom.hasElementWithClassName(e,g)?!1:(d=d.selection.getRange(),d?b(f,g,h).isAppliedToRange(d):!1)},value:function(){}}}(wysihtml5),function(a){a.commands.insertHTML={exec:function(a,b,c){a.commands.support(b)?a.doc.execCommand(b,!1,c):a.selection.insertHTML(c)},state:function(){return!1},value:function(){}}}(wysihtml5),function(a){a.commands.insertImage={exec:function(b,c,d){var e,d="object"==typeof d?d:{src:d},f=b.doc,c=this.state(b);if(c)b.selection.setBefore(c),d=c.parentNode,d.removeChild(c),a.dom.removeEmptyTextNodes(d),"A"===d.nodeName&&!d.firstChild&&(b.selection.setAfter(d),d.parentNode.removeChild(d)),a.quirks.redraw(b.element);else{c=f.createElement("IMG");for(e in d)c[e]=d[e];b.selection.insertNode(c),a.browser.hasProblemsSettingCaretAfterImg()?(d=f.createTextNode(a.INVISIBLE_SPACE),b.selection.insertNode(d),b.selection.setAfter(d)):b.selection.setAfter(c)}},state:function(b){var c;return a.dom.hasElementWithTagName(b.doc,"IMG")?(c=b.selection.getSelectedNode())?"IMG"===c.nodeName?c:c.nodeType!==a.ELEMENT_NODE?!1:(c=b.selection.getText(),(c=a.lang.string(c).trim())?!1:(b=b.selection.getNodes(a.ELEMENT_NODE,function(a){return"IMG"===a.nodeName}),1!==b.length?!1:b[0])):!1:!1},value:function(a){return(a=this.state(a))&&a.src}}}(wysihtml5),function(a){var b="<br>"+(a.browser.needsSpaceAfterLineBreak()?" ":"");a.commands.insertLineBreak={exec:function(c,d){c.commands.support(d)?(c.doc.execCommand(d,!1,null),a.browser.autoScrollsToCaret()||c.selection.scrollIntoView()):c.commands.exec("insertHTML",b)},state:function(){return!1},value:function(){}}}(wysihtml5),function(a){a.commands.insertOrderedList={exec:function(b,c){var d,e=b.doc,f=b.selection.getSelectedNode(),g=a.dom.getParentElement(f,{nodeName:"OL"}),h=a.dom.getParentElement(f,{nodeName:"UL"}),f="_wysihtml5-temp-"+(new Date).getTime();b.commands.support(c)?e.execCommand(c,!1,null):g?b.selection.executeAndRestoreSimple(function(){a.dom.resolveList(g)}):h?b.selection.executeAndRestoreSimple(function(){a.dom.renameElement(h,"ol")}):(b.commands.exec("formatBlock","div",f),d=e.querySelector("."+f),e=""===d.innerHTML||d.innerHTML===a.INVISIBLE_SPACE,b.selection.executeAndRestoreSimple(function(){g=a.dom.convertToList(d,"ol")}),e&&b.selection.selectNode(g.querySelector("li")))},state:function(b){return b=b.selection.getSelectedNode(),a.dom.getParentElement(b,{nodeName:"OL"})},value:function(){}}}(wysihtml5),function(a){a.commands.insertUnorderedList={exec:function(b,c){var d,e=b.doc,f=b.selection.getSelectedNode(),g=a.dom.getParentElement(f,{nodeName:"UL"}),h=a.dom.getParentElement(f,{nodeName:"OL"}),f="_wysihtml5-temp-"+(new Date).getTime();b.commands.support(c)?e.execCommand(c,!1,null):g?b.selection.executeAndRestoreSimple(function(){a.dom.resolveList(g)}):h?b.selection.executeAndRestoreSimple(function(){a.dom.renameElement(h,"ul")}):(b.commands.exec("formatBlock","div",f),d=e.querySelector("."+f),e=""===d.innerHTML||d.innerHTML===a.INVISIBLE_SPACE,b.selection.executeAndRestoreSimple(function(){g=a.dom.convertToList(d,"ul")}),e&&b.selection.selectNode(g.querySelector("li")))},state:function(b){return b=b.selection.getSelectedNode(),a.dom.getParentElement(b,{nodeName:"UL"})},value:function(){}}}(wysihtml5),function(a){a.commands.italic={exec:function(b,c){return a.commands.formatInline.exec(b,c,"i")},state:function(b,c){return a.commands.formatInline.state(b,c,"i")},value:function(){}}}(wysihtml5),function(a){var b=/wysiwyg-text-align-[a-z]+/g;a.commands.justifyCenter={exec:function(c){return a.commands.formatBlock.exec(c,"formatBlock",null,"wysiwyg-text-align-center",b)},state:function(c){return a.commands.formatBlock.state(c,"formatBlock",null,"wysiwyg-text-align-center",b)},value:function(){}}}(wysihtml5),function(a){var b=/wysiwyg-text-align-[a-z]+/g;a.commands.justifyLeft={exec:function(c){return a.commands.formatBlock.exec(c,"formatBlock",null,"wysiwyg-text-align-left",b)},state:function(c){return a.commands.formatBlock.state(c,"formatBlock",null,"wysiwyg-text-align-left",b)},value:function(){}}}(wysihtml5),function(a){var b=/wysiwyg-text-align-[a-z]+/g;a.commands.justifyRight={exec:function(c){return a.commands.formatBlock.exec(c,"formatBlock",null,"wysiwyg-text-align-right",b)},state:function(c){return a.commands.formatBlock.state(c,"formatBlock",null,"wysiwyg-text-align-right",b)},value:function(){}}}(wysihtml5),function(a){a.commands.underline={exec:function(b,c){return a.commands.formatInline.exec(b,c,"u")},state:function(b,c){return a.commands.formatInline.state(b,c,"u")},value:function(){}}}(wysihtml5),function(a){var b='<span id="_wysihtml5-undo" class="_wysihtml5-temp">'+a.INVISIBLE_SPACE+"</span>",c='<span id="_wysihtml5-redo" class="_wysihtml5-temp">'+a.INVISIBLE_SPACE+"</span>",d=a.dom;a.UndoManager=a.lang.Dispatcher.extend({constructor:function(a){this.editor=a,this.composer=a.composer,this.element=this.composer.element,this.history=[this.composer.getValue()],this.position=1,this.composer.commands.support("insertHTML")&&this._observe()},_observe:function(){var e,f=this,g=this.composer.sandbox.getDocument();if(d.observe(this.element,"keydown",function(a){if(!a.altKey&&(a.ctrlKey||a.metaKey)){var b=a.keyCode,c=90===b&&a.shiftKey||89===b;90!==b||a.shiftKey?c&&(f.redo(),a.preventDefault()):(f.undo(),a.preventDefault())}}),d.observe(this.element,"keydown",function(a){a=a.keyCode,a!==e&&(e=a,(8===a||46===a)&&f.transact())}),a.browser.hasUndoInContextMenu()){var h,i,j=function(){for(var a;a=g.querySelector("._wysihtml5-temp");)a.parentNode.removeChild(a);clearInterval(h)};d.observe(this.element,"contextmenu",function(){j(),f.composer.selection.executeAndRestoreSimple(function(){f.element.lastChild&&f.composer.selection.setAfter(f.element.lastChild),g.execCommand("insertHTML",!1,b),g.execCommand("insertHTML",!1,c),g.execCommand("undo",!1,null)}),h=setInterval(function(){g.getElementById("_wysihtml5-redo")?(j(),f.redo()):g.getElementById("_wysihtml5-undo")||(j(),f.undo())},400),i||(i=!0,d.observe(document,"mousedown",j),d.observe(g,["mousedown","paste","cut","copy"],j))})}this.editor.observe("newword:composer",function(){f.transact()}).observe("beforecommand:composer",function(){f.transact()})},transact:function(){var a=this.history[this.position-1],b=this.composer.getValue();b!=a&&(40<(this.history.length=this.position)&&(this.history.shift(),this.position--),this.position++,this.history.push(b))},undo:function(){this.transact(),1>=this.position||(this.set(this.history[--this.position-1]),this.editor.fire("undo:composer"))},redo:function(){this.position>=this.history.length||(this.set(this.history[++this.position-1]),this.editor.fire("redo:composer"))},set:function(a){this.composer.setValue(a),this.editor.focus(!0)}})}(wysihtml5),wysihtml5.views.View=Base.extend({constructor:function(a,b,c){this.parent=a,this.element=b,this.config=c,this._observeViewChange()},_observeViewChange:function(){var a=this;this.parent.observe("beforeload",function(){a.parent.observe("change_view",function(b){b===a.name?(a.parent.currentView=a,a.show(),setTimeout(function(){a.focus()},0)):a.hide()})})},focus:function(){if(this.element.ownerDocument.querySelector(":focus")!==this.element)try{this.element.focus()}catch(a){}},hide:function(){this.element.style.display="none"},show:function(){this.element.style.display=""},disable:function(){this.element.setAttribute("disabled","disabled")},enable:function(){this.element.removeAttribute("disabled")}}),function(a){var b=a.dom,c=a.browser;a.views.Composer=a.views.View.extend({name:"composer",CARET_HACK:"<br>",constructor:function(a,b,c){this.base(a,b,c),this.textarea=this.parent.textarea,this._initSandbox()},clear:function(){this.element.innerHTML=c.displaysCaretInEmptyContentEditableCorrectly()?"":this.CARET_HACK},getValue:function(b){var c=this.isEmpty()?"":a.quirks.getCorrectInnerHTML(this.element);return b&&(c=this.parent.parse(c)),c=a.lang.string(c).replace(a.INVISIBLE_SPACE).by("")},setValue:function(a,b){b&&(a=this.parent.parse(a)),this.element.innerHTML=a},show:function(){this.iframe.style.display=this._displayStyle||"",this.disable(),this.enable()},hide:function(){this._displayStyle=b.getStyle("display").from(this.iframe),"none"===this._displayStyle&&(this._displayStyle=null),this.iframe.style.display="none"},disable:function(){this.element.removeAttribute("contentEditable"),this.base()},enable:function(){this.element.setAttribute("contentEditable","true"),this.base()},focus:function(b){a.browser.doesAsyncFocus()&&this.hasPlaceholderSet()&&this.clear(),this.base();var c=this.element.lastChild;b&&c&&("BR"===c.nodeName?this.selection.setBefore(this.element.lastChild):this.selection.setAfter(this.element.lastChild))},getTextContent:function(){return b.getTextContent(this.element)},hasPlaceholderSet:function(){return this.getTextContent()==this.textarea.element.getAttribute("placeholder")},isEmpty:function(){var a=this.element.innerHTML;return""===a||a===this.CARET_HACK||this.hasPlaceholderSet()||""===this.getTextContent()&&!this.element.querySelector("blockquote, ul, ol, img, embed, object, table, iframe, svg, video, audio, button, input, select, textarea")},_initSandbox:function(){var a=this;this.sandbox=new b.Sandbox(function(){a._create()},{stylesheets:this.config.stylesheets}),this.iframe=this.sandbox.getIframe();var c=document.createElement("input");c.type="hidden",c.name="_wysihtml5_mode",c.value=1;var d=this.textarea.element;b.insert(this.iframe).after(d),b.insert(c).after(d)},_create:function(){var d=this;this.doc=this.sandbox.getDocument(),this.element=this.doc.body,this.textarea=this.parent.textarea,this.element.innerHTML=this.textarea.getValue(!0),this.enable(),this.selection=new a.Selection(this.parent),this.commands=new a.Commands(this.parent),b.copyAttributes("className spellcheck title lang dir accessKey".split(" ")).from(this.textarea.element).to(this.element),b.addClass(this.element,this.config.composerClassName),this.config.style&&this.style(),this.observe();var e=this.config.name;e&&(b.addClass(this.element,e),b.addClass(this.iframe,e)),(e="string"==typeof this.config.placeholder?this.config.placeholder:this.textarea.element.getAttribute("placeholder"))&&b.simulatePlaceholder(this.parent,this,e),this.commands.exec("styleWithCSS",!1),this._initAutoLinking(),this._initObjectResizing(),this._initUndoManager(),(this.textarea.element.hasAttribute("autofocus")||document.querySelector(":focus")==this.textarea.element)&&setTimeout(function(){d.focus()},100),a.quirks.insertLineBreakOnReturn(this),c.clearsContentEditableCorrectly()||a.quirks.ensureProperClearing(this),c.clearsListsInContentEditableCorrectly()||a.quirks.ensureProperClearingOfLists(this),this.initSync&&this.config.sync&&this.initSync(),this.textarea.hide(),this.parent.fire("beforeload").fire("load")},_initAutoLinking:function(){var d=this,e=c.canDisableAutoLinking(),f=c.doesAutoLinkingInContentEditable();if(e&&this.commands.exec("autoUrlDetect",!1),this.config.autoLink){(!f||f&&e)&&this.parent.observe("newword:composer",function(){d.selection.executeAndRestore(function(a,c){b.autoLink(c.parentNode)})});var g=this.sandbox.getDocument().getElementsByTagName("a"),h=b.autoLink.URL_REG_EXP,i=function(c){return c=a.lang.string(b.getTextContent(c)).trim(),"www."===c.substr(0,4)&&(c="http://"+c),c};b.observe(this.element,"keydown",function(a){if(g.length){var c,a=d.selection.getSelectedNode(a.target.ownerDocument),e=b.getParentElement(a,{nodeName:"A"},4);e&&(c=i(e),setTimeout(function(){var a=i(e);a!==c&&a.match(h)&&e.setAttribute("href",a)},0))}})}},_initObjectResizing:function(){var d=["width","height"],e=d.length,f=this.element;this.commands.exec("enableObjectResizing",this.config.allowObjectResizing),this.config.allowObjectResizing?c.supportsEvent("resizeend")&&b.observe(f,"resizeend",function(b){for(var c,b=b.target||b.srcElement,g=b.style,h=0;e>h;h++)c=d[h],g[c]&&(b.setAttribute(c,parseInt(g[c],10)),g[c]="");a.quirks.redraw(f)}):c.supportsEvent("resizestart")&&b.observe(f,"resizestart",function(a){a.preventDefault()})},_initUndoManager:function(){new a.UndoManager(this.parent)}})}(wysihtml5),function(a){var b=a.dom,c=document,d=window,e=c.createElement("div"),f="background-color color cursor font-family font-size font-style font-variant font-weight line-height letter-spacing text-align text-decoration text-indent text-rendering word-break word-wrap word-spacing".split(" "),g="background-color border-collapse border-bottom-color border-bottom-style border-bottom-width border-left-color border-left-style border-left-width border-right-color border-right-style border-right-width border-top-color border-top-style border-top-width clear display float margin-bottom margin-left margin-right margin-top outline-color outline-offset outline-width outline-style padding-left padding-right padding-top padding-bottom position top left right bottom z-index vertical-align text-align -webkit-box-sizing -moz-box-sizing -ms-box-sizing box-sizing -webkit-box-shadow -moz-box-shadow -ms-box-shadow box-shadow -webkit-border-top-right-radius -moz-border-radius-topright border-top-right-radius -webkit-border-bottom-right-radius -moz-border-radius-bottomright border-bottom-right-radius -webkit-border-bottom-left-radius -moz-border-radius-bottomleft border-bottom-left-radius -webkit-border-top-left-radius -moz-border-radius-topleft border-top-left-radius width height".split(" "),h="width height top left right bottom".split(" "),i=["html             { height: 100%; }","body             { min-height: 100%; padding: 0; margin: 0; margin-top: -1px; padding-top: 1px; }","._wysihtml5-temp { display: none; }",a.browser.isGecko?"body.placeholder { color: graytext !important; }":"body.placeholder { color: #a9a9a9 !important; }","body[disabled]   { background-color: #eee !important; color: #999 !important; cursor: default !important; }","img:-moz-broken  { -moz-force-broken-image-icon: 1; height: 24px; width: 24px; }"],j=function(a){if(a.setActive)try{a.setActive()}catch(e){}else{var f=a.style,g=c.documentElement.scrollTop||c.body.scrollTop,h=c.documentElement.scrollLeft||c.body.scrollLeft,f={position:f.position,top:f.top,left:f.left,WebkitUserSelect:f.WebkitUserSelect};b.setStyles({position:"absolute",top:"-99999px",left:"-99999px",WebkitUserSelect:"none"}).on(a),a.focus(),b.setStyles(f).on(a),d.scrollTo&&d.scrollTo(h,g)}};a.views.Composer.prototype.style=function(){var k=this,l=c.querySelector(":focus"),m=this.textarea.element,n=m.hasAttribute("placeholder"),o=n&&m.getAttribute("placeholder");this.focusStylesHost=this.focusStylesHost||e.cloneNode(!1),this.blurStylesHost=this.blurStylesHost||e.cloneNode(!1),n&&m.removeAttribute("placeholder"),m===l&&m.blur(),b.copyStyles(g).from(m).to(this.iframe).andTo(this.blurStylesHost),b.copyStyles(f).from(m).to(this.element).andTo(this.blurStylesHost),b.insertCSS(i).into(this.element.ownerDocument),j(m),b.copyStyles(g).from(m).to(this.focusStylesHost),b.copyStyles(f).from(m).to(this.focusStylesHost);
var p=a.lang.array(g).without(["display"]);if(l?l.focus():m.blur(),n&&m.setAttribute("placeholder",o),!a.browser.hasCurrentStyleProperty())var q=b.observe(d,"resize",function(){if(b.contains(document.documentElement,k.iframe)){var a=b.getStyle("display").from(m),c=b.getStyle("display").from(k.iframe);m.style.display="",k.iframe.style.display="none",b.copyStyles(h).from(m).to(k.iframe).andTo(k.focusStylesHost).andTo(k.blurStylesHost),k.iframe.style.display=c,m.style.display=a}else q.stop()});return this.parent.observe("focus:composer",function(){b.copyStyles(p).from(k.focusStylesHost).to(k.iframe),b.copyStyles(f).from(k.focusStylesHost).to(k.element)}),this.parent.observe("blur:composer",function(){b.copyStyles(p).from(k.blurStylesHost).to(k.iframe),b.copyStyles(f).from(k.blurStylesHost).to(k.element)}),this}}(wysihtml5),function(a){var b=a.dom,c=a.browser,d={66:"bold",73:"italic",85:"underline"};a.views.Composer.prototype.observe=function(){var e=this,f=this.getValue(),g=this.sandbox.getIframe(),h=this.element,i=c.supportsEventsInIframeCorrectly()?h:this.sandbox.getWindow(),j=c.supportsEvent("drop")?["drop","paste"]:["dragdrop","paste"];b.observe(g,"DOMNodeRemoved",function(){clearInterval(k),e.parent.fire("destroy:composer")});var k=setInterval(function(){b.contains(document.documentElement,g)||(clearInterval(k),e.parent.fire("destroy:composer"))},250);b.observe(i,"focus",function(){e.parent.fire("focus").fire("focus:composer"),setTimeout(function(){f=e.getValue()},0)}),b.observe(i,"blur",function(){f!==e.getValue()&&e.parent.fire("change").fire("change:composer"),e.parent.fire("blur").fire("blur:composer")}),a.browser.isIos()&&b.observe(h,"blur",function(){var a=h.ownerDocument.createElement("input"),b=document.documentElement.scrollTop||document.body.scrollTop,c=document.documentElement.scrollLeft||document.body.scrollLeft;try{e.selection.insertNode(a)}catch(d){h.appendChild(a)}a.focus(),a.parentNode.removeChild(a),window.scrollTo(c,b)}),b.observe(h,"dragenter",function(){e.parent.fire("unset_placeholder")}),c.firesOnDropOnlyWhenOnDragOverIsCancelled()&&b.observe(h,["dragover","dragenter"],function(a){a.preventDefault()}),b.observe(h,j,function(a){var b,d=a.dataTransfer;d&&c.supportsDataTransfer()&&(b=d.getData("text/html")||d.getData("text/plain")),b?(h.focus(),e.commands.exec("insertHTML",b),e.parent.fire("paste").fire("paste:composer"),a.stopPropagation(),a.preventDefault()):setTimeout(function(){e.parent.fire("paste").fire("paste:composer")},0)}),b.observe(h,"keyup",function(b){b=b.keyCode,(b===a.SPACE_KEY||b===a.ENTER_KEY)&&e.parent.fire("newword:composer")}),this.parent.observe("paste:composer",function(){setTimeout(function(){e.parent.fire("newword:composer")},0)}),c.canSelectImagesInContentEditable()||b.observe(h,"mousedown",function(a){var b=a.target;"IMG"===b.nodeName&&(e.selection.selectNode(b),a.preventDefault())}),b.observe(h,"keydown",function(a){var b=d[a.keyCode];(a.ctrlKey||a.metaKey)&&!a.altKey&&b&&(e.commands.exec(b),a.preventDefault())}),b.observe(h,"keydown",function(b){var c=e.selection.getSelectedNode(!0),d=b.keyCode;!c||"IMG"!==c.nodeName||d!==a.BACKSPACE_KEY&&d!==a.DELETE_KEY||(d=c.parentNode,d.removeChild(c),"A"===d.nodeName&&!d.firstChild&&d.parentNode.removeChild(d),setTimeout(function(){a.quirks.redraw(h)},0),b.preventDefault())});var l={IMG:"Image: ",A:"Link: "};b.observe(h,"mouseover",function(a){var a=a.target,b=a.nodeName;!("A"!==b&&"IMG"!==b)&&!a.hasAttribute("title")&&(b=l[b]+(a.getAttribute("href")||a.getAttribute("src")),a.setAttribute("title",b))})}}(wysihtml5),function(a){a.views.Synchronizer=Base.extend({constructor:function(a,b,c){this.editor=a,this.textarea=b,this.composer=c,this._observe()},fromComposerToTextarea:function(b){this.textarea.setValue(a.lang.string(this.composer.getValue()).trim(),b)},fromTextareaToComposer:function(a){var b=this.textarea.getValue();b?this.composer.setValue(b,a):(this.composer.clear(),this.editor.fire("set_placeholder"))},sync:function(a){"textarea"===this.editor.currentView.name?this.fromTextareaToComposer(a):this.fromComposerToTextarea(a)},_observe:function(){var b,c=this,d=this.textarea.element.form,e=function(){b=setInterval(function(){c.fromComposerToTextarea()},400)},f=function(){clearInterval(b),b=null};e(),d&&(a.dom.observe(d,"submit",function(){c.sync(!0)}),a.dom.observe(d,"reset",function(){setTimeout(function(){c.fromTextareaToComposer()},0)})),this.editor.observe("change_view",function(a){"composer"!==a||b?"textarea"===a&&(c.fromComposerToTextarea(!0),f()):(c.fromTextareaToComposer(!0),e())}),this.editor.observe("destroy:composer",f)}})}(wysihtml5),wysihtml5.views.Textarea=wysihtml5.views.View.extend({name:"textarea",constructor:function(a,b,c){this.base(a,b,c),this._observe()},clear:function(){this.element.value=""},getValue:function(a){var b=this.isEmpty()?"":this.element.value;return a&&(b=this.parent.parse(b)),b},setValue:function(a,b){b&&(a=this.parent.parse(a)),this.element.value=a},hasPlaceholderSet:function(){var a=wysihtml5.browser.supportsPlaceholderAttributeOn(this.element),b=this.element.getAttribute("placeholder")||null,c=this.element.value;return a&&!c||c===b},isEmpty:function(){return!wysihtml5.lang.string(this.element.value).trim()||this.hasPlaceholderSet()},_observe:function(){var a=this.element,b=this.parent,c={focusin:"focus",focusout:"blur"},d=wysihtml5.browser.supportsEvent("focusin")?["focusin","focusout","change"]:["focus","blur","change"];b.observe("beforeload",function(){wysihtml5.dom.observe(a,d,function(a){a=c[a.type]||a.type,b.fire(a).fire(a+":textarea")}),wysihtml5.dom.observe(a,["paste","drop"],function(){setTimeout(function(){b.fire("paste").fire("paste:textarea")},0)})})}}),function(a){var b=a.dom;a.toolbar.Dialog=a.lang.Dispatcher.extend({constructor:function(a,b){this.link=a,this.container=b},_observe:function(){if(!this._observed){var c=this,d=function(a){var b=c._serialize();b==c.elementToChange?c.fire("edit",b):c.fire("save",b),c.hide(),a.preventDefault(),a.stopPropagation()};b.observe(c.link,"click",function(){b.hasClass(c.link,"wysihtml5-command-dialog-opened")&&setTimeout(function(){c.hide()},0)}),b.observe(this.container,"keydown",function(b){var e=b.keyCode;e===a.ENTER_KEY&&d(b),e===a.ESCAPE_KEY&&c.hide()}),b.delegate(this.container,"[data-wysihtml5-dialog-action=save]","click",d),b.delegate(this.container,"[data-wysihtml5-dialog-action=cancel]","click",function(a){c.fire("cancel"),c.hide(),a.preventDefault(),a.stopPropagation()});for(var e=this.container.querySelectorAll("input, select, textarea"),f=0,g=e.length,h=function(){clearInterval(c.interval)};g>f;f++)b.observe(e[f],"change",h);this._observed=!0}},_serialize:function(){for(var a=this.elementToChange||{},b=this.container.querySelectorAll("[data-wysihtml5-dialog-field]"),c=b.length,d=0;c>d;d++)a[b[d].getAttribute("data-wysihtml5-dialog-field")]=b[d].value;return a},_interpolate:function(a){for(var b,c,d=document.querySelector(":focus"),e=this.container.querySelectorAll("[data-wysihtml5-dialog-field]"),f=e.length,g=0;f>g;g++)b=e[g],b!==d&&!(a&&"hidden"===b.type)&&(c=b.getAttribute("data-wysihtml5-dialog-field"),c=this.elementToChange?this.elementToChange[c]||"":b.defaultValue,b.value=c)},show:function(a){var c=this,d=this.container.querySelector("input, select, textarea");if(this.elementToChange=a,this._observe(),this._interpolate(),a&&(this.interval=setInterval(function(){c._interpolate(!0)},500)),b.addClass(this.link,"wysihtml5-command-dialog-opened"),this.container.style.display="",this.fire("show"),d&&!a)try{d.focus()}catch(e){}},hide:function(){clearInterval(this.interval),this.elementToChange=null,b.removeClass(this.link,"wysihtml5-command-dialog-opened"),this.container.style.display="none",this.fire("hide")}})}(wysihtml5),function(a){var b=a.dom,c={position:"relative"},d={left:0,margin:0,opacity:0,overflow:"hidden",padding:0,position:"absolute",top:0,zIndex:1},e={cursor:"inherit",fontSize:"50px",height:"50px",marginTop:"-25px",outline:0,padding:0,position:"absolute",right:"-4px",top:"50%"},f={"x-webkit-speech":"",speech:""};a.toolbar.Speech=function(g,h){var i=document.createElement("input");if(a.browser.supportsSpeechApiOn(i)){var j=document.createElement("div");a.lang.object(d).merge({width:h.offsetWidth+"px",height:h.offsetHeight+"px"}),b.insert(i).into(j),b.insert(j).into(h),b.setStyles(e).on(i),b.setAttributes(f).on(i),b.setStyles(d).on(j),b.setStyles(c).on(h),b.observe(i,"onwebkitspeechchange"in i?"webkitspeechchange":"speechchange",function(){g.execCommand("insertText",i.value),i.value=""}),b.observe(i,"click",function(a){b.hasClass(h,"wysihtml5-command-disabled")&&a.preventDefault(),a.stopPropagation()})}else h.style.display="none"}}(wysihtml5),function(a){var b=a.dom;a.toolbar.Toolbar=Base.extend({constructor:function(b,c){this.editor=b,this.container="string"==typeof c?document.getElementById(c):c,this.composer=b.composer,this._getLinks("command"),this._getLinks("action"),this._observe(),this.show();for(var d=this.container.querySelectorAll("[data-wysihtml5-command=insertSpeech]"),e=d.length,f=0;e>f;f++)new a.toolbar.Speech(this,d[f])},_getLinks:function(b){for(var c,d,e,f,g,h=this[b+"Links"]=a.lang.array(this.container.querySelectorAll("[data-wysihtml5-"+b+"]")).get(),i=h.length,j=0,k=this[b+"Mapping"]={};i>j;j++)c=h[j],e=c.getAttribute("data-wysihtml5-"+b),f=c.getAttribute("data-wysihtml5-"+b+"-value"),d=this.container.querySelector("[data-wysihtml5-"+b+"-group='"+e+"']"),g=this._getDialog(c,e),k[e+":"+f]={link:c,group:d,name:e,value:f,dialog:g,state:!1}},_getDialog:function(b,c){var d,e,f=this,g=this.container.querySelector("[data-wysihtml5-dialog='"+c+"']");return g&&(d=new a.toolbar.Dialog(b,g),d.observe("show",function(){e=f.composer.selection.getBookmark(),f.editor.fire("show:dialog",{command:c,dialogContainer:g,commandLink:b})}),d.observe("save",function(a){e&&f.composer.selection.setBookmark(e),f._execCommand(c,a),f.editor.fire("save:dialog",{command:c,dialogContainer:g,commandLink:b})}),d.observe("cancel",function(){f.editor.focus(!1),f.editor.fire("cancel:dialog",{command:c,dialogContainer:g,commandLink:b})})),d},execCommand:function(a,b){if(!this.commandsDisabled){var c=this.commandMapping[a+":"+b];c&&c.dialog&&!c.state?c.dialog.show():this._execCommand(a,b)}},_execCommand:function(a,b){this.editor.focus(!1),this.composer.commands.exec(a,b),this._updateLinkStates()},execAction:function(a){var b=this.editor;switch(a){case"change_view":b.currentView===b.textarea?b.fire("change_view","composer"):b.fire("change_view","textarea")}},_observe:function(){for(var a=this,c=this.editor,d=this.container,e=this.commandLinks.concat(this.actionLinks),f=e.length,g=0;f>g;g++)b.setAttributes({href:"javascript:;",unselectable:"on"}).on(e[g]);b.delegate(d,"[data-wysihtml5-command]","mousedown",function(a){a.preventDefault()}),b.delegate(d,"[data-wysihtml5-command]","click",function(b){var c=this.getAttribute("data-wysihtml5-command"),d=this.getAttribute("data-wysihtml5-command-value");a.execCommand(c,d),b.preventDefault()}),b.delegate(d,"[data-wysihtml5-action]","click",function(b){var c=this.getAttribute("data-wysihtml5-action");a.execAction(c),b.preventDefault()}),c.observe("focus:composer",function(){a.bookmark=null,clearInterval(a.interval),a.interval=setInterval(function(){a._updateLinkStates()},500)}),c.observe("blur:composer",function(){clearInterval(a.interval)}),c.observe("destroy:composer",function(){clearInterval(a.interval)}),c.observe("change_view",function(c){setTimeout(function(){a.commandsDisabled="composer"!==c,a._updateLinkStates(),a.commandsDisabled?b.addClass(d,"wysihtml5-commands-disabled"):b.removeClass(d,"wysihtml5-commands-disabled")},0)})},_updateLinkStates:function(){var c,d,e,f=this.commandMapping,g=this.actionMapping;for(c in f)e=f[c],this.commandsDisabled?(d=!1,b.removeClass(e.link,"wysihtml5-command-active"),e.group&&b.removeClass(e.group,"wysihtml5-command-active"),e.dialog&&e.dialog.hide()):(d=this.composer.commands.state(e.name,e.value),a.lang.object(d).isArray()&&(d=1===d.length?d[0]:!0),b.removeClass(e.link,"wysihtml5-command-disabled"),e.group&&b.removeClass(e.group,"wysihtml5-command-disabled")),e.state!==d&&((e.state=d)?(b.addClass(e.link,"wysihtml5-command-active"),e.group&&b.addClass(e.group,"wysihtml5-command-active"),e.dialog&&("object"==typeof d?e.dialog.show(d):e.dialog.hide())):(b.removeClass(e.link,"wysihtml5-command-active"),e.group&&b.removeClass(e.group,"wysihtml5-command-active"),e.dialog&&e.dialog.hide()));for(c in g)f=g[c],"change_view"===f.name&&(f.state=this.editor.currentView===this.editor.textarea,f.state?b.addClass(f.link,"wysihtml5-action-active"):b.removeClass(f.link,"wysihtml5-action-active"))},show:function(){this.container.style.display=""},hide:function(){this.container.style.display="none"}})}(wysihtml5),function(a){var b={name:void 0,style:!0,toolbar:void 0,autoLink:!0,parserRules:{tags:{br:{},span:{},div:{},p:{}},classes:{}},parser:a.dom.parse,composerClassName:"wysihtml5-editor",bodyClassName:"wysihtml5-supported",stylesheets:[],placeholderText:void 0,allowObjectResizing:!0,supportTouchDevices:!0};a.Editor=a.lang.Dispatcher.extend({constructor:function(c,d){if(this.textareaElement="string"==typeof c?document.getElementById(c):c,this.config=a.lang.object({}).merge(b).merge(d).get(),this.currentView=this.textarea=new a.views.Textarea(this,this.textareaElement,this.config),this._isCompatible=a.browser.supported(),!this._isCompatible||!this.config.supportTouchDevices&&a.browser.isTouchDevice()){var e=this;setTimeout(function(){e.fire("beforeload").fire("load")},0)}else{a.dom.addClass(document.body,this.config.bodyClassName),this.currentView=this.composer=new a.views.Composer(this,this.textareaElement,this.config),"function"==typeof this.config.parser&&this._initParser(),this.observe("beforeload",function(){this.synchronizer=new a.views.Synchronizer(this,this.textarea,this.composer),this.config.toolbar&&(this.toolbar=new a.toolbar.Toolbar(this,this.config.toolbar))});try{console.log("Heya! This page is using wysihtml5 for rich text editing. Check out https://github.com/xing/wysihtml5")}catch(f){}}},isCompatible:function(){return this._isCompatible},clear:function(){return this.currentView.clear(),this},getValue:function(a){return this.currentView.getValue(a)},setValue:function(a,b){return a?(this.currentView.setValue(a,b),this):this.clear()},focus:function(a){return this.currentView.focus(a),this},disable:function(){return this.currentView.disable(),this},enable:function(){return this.currentView.enable(),this},isEmpty:function(){return this.currentView.isEmpty()},hasPlaceholderSet:function(){return this.currentView.hasPlaceholderSet()},parse:function(b){var c=this.config.parser(b,this.config.parserRules,this.composer.sandbox.getDocument(),!0);return"object"==typeof b&&a.quirks.redraw(b),c},_initParser:function(){this.observe("paste:composer",function(){var b=this;b.composer.selection.executeAndRestore(function(){a.quirks.cleanPastedHTML(b.composer.element),b.parse(b.composer.element)},!0)}),this.observe("paste:textarea",function(){this.textarea.setValue(this.parse(this.textarea.getValue()))})}})}(wysihtml5);var Handlebars=function(){var a=function(){"use strict";function a(a){this.string=a}var b;return a.prototype.toString=function(){return""+this.string},b=a}(),b=function(a){"use strict";function b(a){return h[a]||"&amp;"}function c(a,b){for(var c in b)b.hasOwnProperty(c)&&(a[c]=b[c])}function d(a){return a instanceof g?a.toString():a||0===a?(a=""+a,j.test(a)?a.replace(i,b):a):""}function e(a){return a||0===a?m(a)&&0===a.length?!0:!1:!0}var f={},g=a,h={"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#x27;","`":"&#x60;"},i=/[&<>"'`]/g,j=/[&<>"'`]/;f.extend=c;var k=Object.prototype.toString;f.toString=k;var l=function(a){return"function"==typeof a};l(/x/)&&(l=function(a){return"function"==typeof a&&"[object Function]"===k.call(a)});var l;f.isFunction=l;var m=Array.isArray||function(a){return a&&"object"==typeof a?"[object Array]"===k.call(a):!1};return f.isArray=m,f.escapeExpression=d,f.isEmpty=e,f}(a),c=function(){"use strict";function a(){for(var a=Error.prototype.constructor.apply(this,arguments),b=0;b<c.length;b++)this[c[b]]=a[c[b]]}var b,c=["description","fileName","lineNumber","message","name","number","stack"];return a.prototype=new Error,b=a}(),d=function(a,b){"use strict";function c(a,b){this.helpers=a||{},this.partials=b||{},d(this)}function d(a){a.registerHelper("helperMissing",function(a){if(2===arguments.length)return void 0;throw new Error("Missing helper: '"+a+"'")}),a.registerHelper("blockHelperMissing",function(b,c){var d=c.inverse||function(){},e=c.fn;return m(b)&&(b=b.call(this)),b===!0?e(this):b===!1||null==b?d(this):l(b)?b.length>0?a.helpers.each(b,c):d(this):e(b)}),a.registerHelper("each",function(a,b){var c,d=b.fn,e=b.inverse,f=0,g="";if(m(a)&&(a=a.call(this)),b.data&&(c=q(b.data)),a&&"object"==typeof a)if(l(a))for(var h=a.length;h>f;f++)c&&(c.index=f,c.first=0===f,c.last=f===a.length-1),g+=d(a[f],{data:c});else for(var i in a)a.hasOwnProperty(i)&&(c&&(c.key=i),g+=d(a[i],{data:c}),f++);return 0===f&&(g=e(this)),g}),a.registerHelper("if",function(a,b){return m(a)&&(a=a.call(this)),!b.hash.includeZero&&!a||g.isEmpty(a)?b.inverse(this):b.fn(this)}),a.registerHelper("unless",function(b,c){return a.helpers["if"].call(this,b,{fn:c.inverse,inverse:c.fn,hash:c.hash})}),a.registerHelper("with",function(a,b){return m(a)&&(a=a.call(this)),g.isEmpty(a)?void 0:b.fn(a)}),a.registerHelper("log",function(b,c){var d=c.data&&null!=c.data.level?parseInt(c.data.level,10):1;a.log(d,b)})}function e(a,b){p.log(a,b)}var f={},g=a,h=b,i="1.1.2";f.VERSION=i;var j=4;f.COMPILER_REVISION=j;var k={1:"<= 1.0.rc.2",2:"== 1.0.0-rc.3",3:"== 1.0.0-rc.4",4:">= 1.0.0"};f.REVISION_CHANGES=k;var l=g.isArray,m=g.isFunction,n=g.toString,o="[object Object]";f.HandlebarsEnvironment=c,c.prototype={constructor:c,logger:p,log:e,registerHelper:function(a,b,c){if(n.call(a)===o){if(c||b)throw new h("Arg not supported with multiple helpers");g.extend(this.helpers,a)}else c&&(b.not=c),this.helpers[a]=b},registerPartial:function(a,b){n.call(a)===o?g.extend(this.partials,a):this.partials[a]=b}};var p={methodMap:{0:"debug",1:"info",2:"warn",3:"error"},DEBUG:0,INFO:1,WARN:2,ERROR:3,level:3,log:function(a,b){if(p.level<=a){var c=p.methodMap[a];"undefined"!=typeof console&&console[c]&&console[c].call(console,b)}}};f.logger=p,f.log=e;var q=function(a){var b={};return g.extend(b,a),b};return f.createFrame=q,f}(b,c),e=function(a,b,c){"use strict";function d(a){var b=a&&a[0]||1,c=m;if(b!==c){if(c>b){var d=n[c],e=n[b];throw new Error("Template was precompiled with an older version of Handlebars than the current runtime. Please update your precompiler to a newer version ("+d+") or downgrade your runtime to an older version ("+e+").")}throw new Error("Template was precompiled with a newer version of Handlebars than the current runtime. Please update your runtime to a newer version ("+a[1]+").")}}function e(a,b){if(!b)throw new Error("No environment passed to template");var c;c=b.compile?function(a,c,d,e,f,g){var i=h.apply(this,arguments);if(i)return i;var j={helpers:e,partials:f,data:g};return f[c]=b.compile(a,{data:void 0!==g},b),f[c](d,j)}:function(a,b){var c=h.apply(this,arguments);if(c)return c;throw new l("The partial "+b+" could not be compiled when running in runtime-only mode")};var e={escapeExpression:k.escapeExpression,invokePartial:c,programs:[],program:function(a,b,c){var d=this.programs[a];return c?d=g(a,b,c):d||(d=this.programs[a]=g(a,b)),d},merge:function(a,b){var c=a||b;return a&&b&&a!==b&&(c={},k.extend(c,b),k.extend(c,a)),c},programWithDepth:f,noop:i,compilerInfo:null};return function(c,f){f=f||{};var g,h,i=f.partial?f:b;f.partial||(g=f.helpers,h=f.partials);var j=a.call(e,i,c,g,h,f.data);return f.partial||d(e.compilerInfo),j}}function f(a,b,c){var d=Array.prototype.slice.call(arguments,3),e=function(a,e){return e=e||{},b.apply(this,[a,e.data||c].concat(d))};return e.program=a,e.depth=d.length,e}function g(a,b,c){var d=function(a,d){return d=d||{},b(a,d.data||c)};return d.program=a,d.depth=0,d}function h(a,b,c,d,e,f){var g={partial:!0,helpers:d,partials:e,data:f};if(void 0===a)throw new l("The partial "+b+" could not be found");return a instanceof Function?a(c,g):void 0}function i(){return""}var j={},k=a,l=b,m=c.COMPILER_REVISION,n=c.REVISION_CHANGES;return j.template=e,j.programWithDepth=f,j.program=g,j.invokePartial=h,j.noop=i,j}(b,c,d),f=function(a,b,c,d,e){"use strict";var f,g=a,h=b,i=c,j=d,k=e,l=function(){var a=new g.HandlebarsEnvironment;return j.extend(a,g),a.SafeString=h,a.Exception=i,a.Utils=j,a.VM=k,a.template=function(b){return k.template(b,a)},a},m=l();return m.create=l,f=m}(d,a,c,b,e);return f}(),glob="undefined"==typeof window?global:window,Handlebars=glob.Handlebars||require("handlebars");this.wysihtml5=this.wysihtml5||{},this.wysihtml5.tpl=this.wysihtml5.tpl||{},this.wysihtml5.tpl.color=Handlebars.template(function(a,b,c,d,e){function f(a){var b,c="";return c+="btn-"+k((b=a&&a.options,b=null==b||b===!1?b:b.size,typeof b===j?b.apply(a):b))}this.compilerInfo=[4,">= 1.0.0"],c=this.merge(c,a.helpers),e=e||{};var g,h,i="",j="function",k=this.escapeExpression,l=this;return i+='<li class="dropdown">\n  <a class="btn btn-default dropdown-toggle ',h=c["if"].call(b,(g=b&&b.options,null==g||g===!1?g:g.size),{hash:{},inverse:l.noop,fn:l.program(1,f,e),data:e}),(h||0===h)&&(i+=h),i+='" data-toggle="dropdown" tabindex="-1">\n    <span class="current-color">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.colours,g=null==g||g===!1?g:g.black,typeof g===j?g.apply(b):g))+'</span>\n    <b class="caret"></b>\n  </a>\n  <ul class="dropdown-menu">\n    <li><div class="wysihtml5-colors" data-wysihtml5-command-value="black"></div><a class="wysihtml5-colors-title" data-wysihtml5-command="foreColor" data-wysihtml5-command-value="black">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.colours,g=null==g||g===!1?g:g.black,typeof g===j?g.apply(b):g))+'</a></li>\n    <li><div class="wysihtml5-colors" data-wysihtml5-command-value="silver"></div><a class="wysihtml5-colors-title" data-wysihtml5-command="foreColor" data-wysihtml5-command-value="silver">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.colours,g=null==g||g===!1?g:g.silver,typeof g===j?g.apply(b):g))+'</a></li>\n    <li><div class="wysihtml5-colors" data-wysihtml5-command-value="gray"></div><a class="wysihtml5-colors-title" data-wysihtml5-command="foreColor" data-wysihtml5-command-value="gray">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.colours,g=null==g||g===!1?g:g.gray,typeof g===j?g.apply(b):g))+'</a></li>\n    <li><div class="wysihtml5-colors" data-wysihtml5-command-value="maroon"></div><a class="wysihtml5-colors-title" data-wysihtml5-command="foreColor" data-wysihtml5-command-value="maroon">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.colours,g=null==g||g===!1?g:g.maroon,typeof g===j?g.apply(b):g))+'</a></li>\n    <li><div class="wysihtml5-colors" data-wysihtml5-command-value="red"></div><a class="wysihtml5-colors-title" data-wysihtml5-command="foreColor" data-wysihtml5-command-value="red">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.colours,g=null==g||g===!1?g:g.red,typeof g===j?g.apply(b):g))+'</a></li>\n    <li><div class="wysihtml5-colors" data-wysihtml5-command-value="purple"></div><a class="wysihtml5-colors-title" data-wysihtml5-command="foreColor" data-wysihtml5-command-value="purple">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.colours,g=null==g||g===!1?g:g.purple,typeof g===j?g.apply(b):g))+'</a></li>\n    <li><div class="wysihtml5-colors" data-wysihtml5-command-value="green"></div><a class="wysihtml5-colors-title" data-wysihtml5-command="foreColor" data-wysihtml5-command-value="green">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.colours,g=null==g||g===!1?g:g.green,typeof g===j?g.apply(b):g))+'</a></li>\n    <li><div class="wysihtml5-colors" data-wysihtml5-command-value="olive"></div><a class="wysihtml5-colors-title" data-wysihtml5-command="foreColor" data-wysihtml5-command-value="olive">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.colours,g=null==g||g===!1?g:g.olive,typeof g===j?g.apply(b):g))+'</a></li>\n    <li><div class="wysihtml5-colors" data-wysihtml5-command-value="navy"></div><a class="wysihtml5-colors-title" data-wysihtml5-command="foreColor" data-wysihtml5-command-value="navy">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.colours,g=null==g||g===!1?g:g.navy,typeof g===j?g.apply(b):g))+'</a></li>\n    <li><div class="wysihtml5-colors" data-wysihtml5-command-value="blue"></div><a class="wysihtml5-colors-title" data-wysihtml5-command="foreColor" data-wysihtml5-command-value="blue">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.colours,g=null==g||g===!1?g:g.blue,typeof g===j?g.apply(b):g))+'</a></li>\n    <li><div class="wysihtml5-colors" data-wysihtml5-command-value="orange"></div><a class="wysihtml5-colors-title" data-wysihtml5-command="foreColor" data-wysihtml5-command-value="orange">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.colours,g=null==g||g===!1?g:g.orange,typeof g===j?g.apply(b):g))+"</a></li>\n  </ul>\n</li>\n"}),this.wysihtml5.tpl.emphasis=Handlebars.template(function(a,b,c,d,e){function f(a){var b,c="";return c+="btn-"+k((b=a&&a.options,b=null==b||b===!1?b:b.size,typeof b===j?b.apply(a):b))}this.compilerInfo=[4,">= 1.0.0"],c=this.merge(c,a.helpers),e=e||{};var g,h,i="",j="function",k=this.escapeExpression,l=this;return i+='<li>\n  <div class="btn-group">\n    <a class="btn ',h=c["if"].call(b,(g=b&&b.options,null==g||g===!1?g:g.size),{hash:{},inverse:l.noop,fn:l.program(1,f,e),data:e}),(h||0===h)&&(i+=h),i+=' btn-default" data-wysihtml5-command="bold" title="CTRL+B" tabindex="-1">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.emphasis,g=null==g||g===!1?g:g.bold,typeof g===j?g.apply(b):g))+'</a>\n    <a class="btn ',h=c["if"].call(b,(g=b&&b.options,null==g||g===!1?g:g.size),{hash:{},inverse:l.noop,fn:l.program(1,f,e),data:e}),(h||0===h)&&(i+=h),i+=' btn-default" data-wysihtml5-command="italic" title="CTRL+I" tabindex="-1">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.emphasis,g=null==g||g===!1?g:g.italic,typeof g===j?g.apply(b):g))+'</a>\n    <a class="btn ',h=c["if"].call(b,(g=b&&b.options,null==g||g===!1?g:g.size),{hash:{},inverse:l.noop,fn:l.program(1,f,e),data:e}),(h||0===h)&&(i+=h),i+=' btn-default" data-wysihtml5-command="underline" title="CTRL+U" tabindex="-1">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.emphasis,g=null==g||g===!1?g:g.underline,typeof g===j?g.apply(b):g))+"</a>\n  </div>\n</li>\n"}),this.wysihtml5.tpl["font-styles"]=Handlebars.template(function(a,b,c,d,e){function f(a){var b,c="";return c+="btn-"+k((b=a&&a.options,b=null==b||b===!1?b:b.size,typeof b===j?b.apply(a):b))}this.compilerInfo=[4,">= 1.0.0"],c=this.merge(c,a.helpers),e=e||{};var g,h,i="",j="function",k=this.escapeExpression,l=this;return i+='<li class="dropdown">\n  <a class="btn btn-default dropdown-toggle ',h=c["if"].call(b,(g=b&&b.options,null==g||g===!1?g:g.size),{hash:{},inverse:l.noop,fn:l.program(1,f,e),data:e}),(h||0===h)&&(i+=h),i+='" data-toggle="dropdown">\n    <span class="glyphicon glyphicon-font"></span>\n    <span class="current-font">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.font_styles,g=null==g||g===!1?g:g.normal,typeof g===j?g.apply(b):g))+'</span>\n    <b class="caret"></b>\n  </a>\n  <ul class="dropdown-menu">\n    <li><a data-wysihtml5-command="formatBlock" data-wysihtml5-command-value="div" tabindex="-1">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.font_styles,g=null==g||g===!1?g:g.normal,typeof g===j?g.apply(b):g))+'</a></li>\n    <li><a data-wysihtml5-command="formatBlock" data-wysihtml5-command-value="h1" tabindex="-1">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.font_styles,g=null==g||g===!1?g:g.h1,typeof g===j?g.apply(b):g))+'</a></li>\n    <li><a data-wysihtml5-command="formatBlock" data-wysihtml5-command-value="h2" tabindex="-1">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.font_styles,g=null==g||g===!1?g:g.h2,typeof g===j?g.apply(b):g))+'</a></li>\n    <li><a data-wysihtml5-command="formatBlock" data-wysihtml5-command-value="h3" tabindex="-1">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.font_styles,g=null==g||g===!1?g:g.h3,typeof g===j?g.apply(b):g))+'</a></li>\n    <li><a data-wysihtml5-command="formatBlock" data-wysihtml5-command-value="h4" tabindex="-1">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.font_styles,g=null==g||g===!1?g:g.h4,typeof g===j?g.apply(b):g))+'</a></li>\n    <li><a data-wysihtml5-command="formatBlock" data-wysihtml5-command-value="h5" tabindex="-1">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.font_styles,g=null==g||g===!1?g:g.h5,typeof g===j?g.apply(b):g))+'</a></li>\n    <li><a data-wysihtml5-command="formatBlock" data-wysihtml5-command-value="h6" tabindex="-1">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.font_styles,g=null==g||g===!1?g:g.h6,typeof g===j?g.apply(b):g))+"</a></li>\n  </ul>\n</li>\n"}),this.wysihtml5.tpl.html=Handlebars.template(function(a,b,c,d,e){function f(a){var b,c="";return c+="btn-"+k((b=a&&a.options,b=null==b||b===!1?b:b.size,typeof b===j?b.apply(a):b))}this.compilerInfo=[4,">= 1.0.0"],c=this.merge(c,a.helpers),e=e||{};var g,h,i="",j="function",k=this.escapeExpression,l=this;return i+='<li>\n  <div class="btn-group">\n    <a class="btn ',h=c["if"].call(b,(g=b&&b.options,null==g||g===!1?g:g.size),{hash:{},inverse:l.noop,fn:l.program(1,f,e),data:e}),(h||0===h)&&(i+=h),i+=' btn-default" data-wysihtml5-action="change_view" title="'+k((g=b&&b.locale,g=null==g||g===!1?g:g.html,g=null==g||g===!1?g:g.edit,typeof g===j?g.apply(b):g))+'" tabindex="-1">\n      <span class="glyphicon glyphicon-pencil"></span>\n    </a>\n  </div>\n</li>\n'}),this.wysihtml5.tpl.image=Handlebars.template(function(a,b,c,d,e){function f(a){var b,c="";return c+="btn-"+k((b=a&&a.options,b=null==b||b===!1?b:b.size,typeof b===j?b.apply(a):b))}this.compilerInfo=[4,">= 1.0.0"],c=this.merge(c,a.helpers),e=e||{};var g,h,i="",j="function",k=this.escapeExpression,l=this;return i+='<li>\n  <div class="bootstrap-wysihtml5-insert-image-modal modal fade">\n    <div class="modal-dialog">\n      <div class="modal-content">\n        <div class="modal-header">\n          <a class="close" data-dismiss="modal">&times;</a>\n          <h3>'+k((g=b&&b.locale,g=null==g||g===!1?g:g.image,g=null==g||g===!1?g:g.insert,typeof g===j?g.apply(b):g))+'</h3>\n        </div>\n        <div class="modal-body">\n          <input value="http://" class="bootstrap-wysihtml5-insert-image-url form-control">\n        </div>\n        <div class="modal-footer">\n          <a class="btn btn-default" data-dismiss="modal">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.image,g=null==g||g===!1?g:g.cancel,typeof g===j?g.apply(b):g))+'</a>\n          <a class="btn btn-primary" data-dismiss="modal">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.image,g=null==g||g===!1?g:g.insert,typeof g===j?g.apply(b):g))+'</a>\n        </div>\n      </div>\n    </div>\n  </div>\n  <a class="btn ',h=c["if"].call(b,(g=b&&b.options,null==g||g===!1?g:g.size),{hash:{},inverse:l.noop,fn:l.program(1,f,e),data:e}),(h||0===h)&&(i+=h),i+=' btn-default" data-wysihtml5-command="insertImage" title="'+k((g=b&&b.locale,g=null==g||g===!1?g:g.image,g=null==g||g===!1?g:g.insert,typeof g===j?g.apply(b):g))+'" tabindex="-1">\n    <span class="glyphicon glyphicon-picture"></span>\n  </a>\n</li>\n'}),this.wysihtml5.tpl.link=Handlebars.template(function(a,b,c,d,e){function f(a){var b,c="";return c+="btn-"+k((b=a&&a.options,b=null==b||b===!1?b:b.size,typeof b===j?b.apply(a):b))}this.compilerInfo=[4,">= 1.0.0"],c=this.merge(c,a.helpers),e=e||{};var g,h,i="",j="function",k=this.escapeExpression,l=this;return i+='<li>\n  <div class="bootstrap-wysihtml5-insert-link-modal modal fade">\n    <div class="modal-dialog">\n      <div class="modal-content">\n        <div class="modal-header">\n          <a class="close" data-dismiss="modal">&times;</a>\n          <h3>'+k((g=b&&b.locale,g=null==g||g===!1?g:g.link,g=null==g||g===!1?g:g.insert,typeof g===j?g.apply(b):g))+'</h3>\n        </div>\n        <div class="modal-body">\n          <input value="http://" class="bootstrap-wysihtml5-insert-link-url form-control">\n          <label class="checkbox"> <input type="checkbox" class="bootstrap-wysihtml5-insert-link-target" checked>'+k((g=b&&b.locale,g=null==g||g===!1?g:g.link,g=null==g||g===!1?g:g.target,typeof g===j?g.apply(b):g))+'</label>\n        </div>\n        <div class="modal-footer">\n          <a class="btn btn-default" data-dismiss="modal">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.link,g=null==g||g===!1?g:g.cancel,typeof g===j?g.apply(b):g))+'</a>\n          <a href="#" class="btn btn-primary" data-dismiss="modal">'+k((g=b&&b.locale,g=null==g||g===!1?g:g.link,g=null==g||g===!1?g:g.insert,typeof g===j?g.apply(b):g))+'</a>\n        </div>\n      </div>\n    </div>\n  </div>\n  <a class="btn ',h=c["if"].call(b,(g=b&&b.options,null==g||g===!1?g:g.size),{hash:{},inverse:l.noop,fn:l.program(1,f,e),data:e}),(h||0===h)&&(i+=h),i+=' btn-default" data-wysihtml5-command="createLink" title="'+k((g=b&&b.locale,g=null==g||g===!1?g:g.link,g=null==g||g===!1?g:g.insert,typeof g===j?g.apply(b):g))+'" tabindex="-1">\n    <span class="glyphicon glyphicon-share"></span>\n  </a>\n</li>\n'
}),this.wysihtml5.tpl.lists=Handlebars.template(function(a,b,c,d,e){function f(a){var b,c="";return c+="btn-"+k((b=a&&a.options,b=null==b||b===!1?b:b.size,typeof b===j?b.apply(a):b))}this.compilerInfo=[4,">= 1.0.0"],c=this.merge(c,a.helpers),e=e||{};var g,h,i="",j="function",k=this.escapeExpression,l=this;return i+='<li>\n  <div class="btn-group">\n    <a class="btn ',h=c["if"].call(b,(g=b&&b.options,null==g||g===!1?g:g.size),{hash:{},inverse:l.noop,fn:l.program(1,f,e),data:e}),(h||0===h)&&(i+=h),i+=' btn-default" data-wysihtml5-command="insertUnorderedList" title="'+k((g=b&&b.locale,g=null==g||g===!1?g:g.lists,g=null==g||g===!1?g:g.unordered,typeof g===j?g.apply(b):g))+'" tabindex="-1"><span class="glyphicon glyphicon-list"></span></a>\n    <a class="btn ',h=c["if"].call(b,(g=b&&b.options,null==g||g===!1?g:g.size),{hash:{},inverse:l.noop,fn:l.program(1,f,e),data:e}),(h||0===h)&&(i+=h),i+=' btn-default" data-wysihtml5-command="insertOrderedList" title="'+k((g=b&&b.locale,g=null==g||g===!1?g:g.lists,g=null==g||g===!1?g:g.ordered,typeof g===j?g.apply(b):g))+'" tabindex="-1"><span class="glyphicon glyphicon-th-list"></span></a>\n    <a class="btn ',h=c["if"].call(b,(g=b&&b.options,null==g||g===!1?g:g.size),{hash:{},inverse:l.noop,fn:l.program(1,f,e),data:e}),(h||0===h)&&(i+=h),i+=' btn-default" data-wysihtml5-command="Outdent" title="'+k((g=b&&b.locale,g=null==g||g===!1?g:g.lists,g=null==g||g===!1?g:g.outdent,typeof g===j?g.apply(b):g))+'" tabindex="-1"><span class="glyphicon glyphicon-indent-right"></span></a>\n    <a class="btn ',h=c["if"].call(b,(g=b&&b.options,null==g||g===!1?g:g.size),{hash:{},inverse:l.noop,fn:l.program(1,f,e),data:e}),(h||0===h)&&(i+=h),i+=' btn-default" data-wysihtml5-command="Indent" title="'+k((g=b&&b.locale,g=null==g||g===!1?g:g.lists,g=null==g||g===!1?g:g.indent,typeof g===j?g.apply(b):g))+'" tabindex="-1"><span class="glyphicon glyphicon-indent-left"></span></a>\n  </div>\n</li>\n'}),"object"==typeof exports&&exports&&(module.exports=this.wysihtml5.tpl),!function(a,b){"use strict";var c=function(a,c,d){return b.tpl[a]({locale:c,options:d})},d=function(c,d){this.el=c;var e=d||f;for(var g in e.customTemplates)b.tpl[g]=e.customTemplates[g];this.toolbar=this.createToolbar(c,e),this.editor=this.createEditor(d),window.editor=this.editor,a("iframe.wysihtml5-sandbox").each(function(b,c){a(c.contentWindow).off("focus.wysihtml5").on({"focus.wysihtml5":function(){a("li.dropdown").removeClass("open")}})})};d.prototype={constructor:d,createEditor:function(c){c=c||{},c=a.extend(!0,{},c),c.toolbar=this.toolbar[0];var d=new b.Editor(this.el[0],c);if(c&&c.events)for(var e in c.events)d.on(e,c.events[e]);return d},createToolbar:function(b,d){var e=this,h=a("<ul/>",{"class":"wysihtml5-toolbar",style:"display:none"}),i=d.locale||f.locale||"en";for(var j in f){var k=!1;void 0!==d[j]?d[j]===!0&&(k=!0):k=f[j],k===!0&&(h.append(c(j,g[i],d)),"html"===j&&this.initHtml(h),"link"===j&&this.initInsertLink(h),"image"===j&&this.initInsertImage(h))}if(d.toolbar)for(j in d.toolbar)h.append(d.toolbar[j]);return h.find('a[data-wysihtml5-command="formatBlock"]').click(function(b){var c=b.target||b.srcElement,d=a(c);e.toolbar.find(".current-font").text(d.html())}),h.find('a[data-wysihtml5-command="foreColor"]').click(function(b){var c=b.target||b.srcElement,d=a(c);e.toolbar.find(".current-color").text(d.html())}),this.el.before(h),h},initHtml:function(a){var b='a[data-wysihtml5-action="change_view"]';a.find(b).click(function(){a.find("a.btn").not(b).toggleClass("disabled")})},initInsertImage:function(b){var c,d=this,e=b.find(".bootstrap-wysihtml5-insert-image-modal"),f=e.find(".bootstrap-wysihtml5-insert-image-url"),g=e.find("a.btn-primary"),h=f.val(),i=function(){var a=f.val();f.val(h),d.editor.currentView.element.focus(),c&&(d.editor.composer.selection.setBookmark(c),c=null),d.editor.composer.commands.exec("insertImage",a)};f.keypress(function(a){13==a.which&&(i(),e.modal("hide"))}),g.click(i),e.on("shown",function(){f.focus()}),e.on("hide",function(){d.editor.currentView.element.focus()}),b.find("a[data-wysihtml5-command=insertImage]").click(function(){var b=a(this).hasClass("wysihtml5-command-active");return b?!0:(d.editor.currentView.element.focus(!1),c=d.editor.composer.selection.getBookmark(),e.appendTo("body").modal("show"),e.on("click.dismiss.modal",'[data-dismiss="modal"]',function(a){a.stopPropagation()}),!1)})},initInsertLink:function(b){var c,d=this,e=b.find(".bootstrap-wysihtml5-insert-link-modal"),f=e.find(".bootstrap-wysihtml5-insert-link-url"),g=e.find(".bootstrap-wysihtml5-insert-link-target"),h=e.find("a.btn-primary"),i=f.val(),j=function(){var a=f.val();f.val(i),d.editor.currentView.element.focus(),c&&(d.editor.composer.selection.setBookmark(c),c=null);var b=g.prop("checked");d.editor.composer.commands.exec("createLink",{href:a,target:b?"_blank":"_self",rel:b?"nofollow":""})};f.keypress(function(a){13==a.which&&(j(),e.modal("hide"))}),h.click(j),e.on("shown",function(){f.focus()}),e.on("hide",function(){d.editor.currentView.element.focus()}),b.find("a[data-wysihtml5-command=createLink]").click(function(){var b=a(this).hasClass("wysihtml5-command-active");return b?!0:(d.editor.currentView.element.focus(!1),c=d.editor.composer.selection.getBookmark(),e.appendTo("body").modal("show"),e.on("click.dismiss.modal",'[data-dismiss="modal"]',function(a){a.stopPropagation()}),!1)})}};var e={resetDefaults:function(){a.fn.wysihtml5.defaultOptions=a.extend(!0,{},a.fn.wysihtml5.defaultOptionsCache)},bypassDefaults:function(b){return this.each(function(){var c=a(this);c.data("wysihtml5",new d(c,b))})},shallowExtend:function(b){var c=a.extend({},a.fn.wysihtml5.defaultOptions,b||{},a(this).data()),d=this;return e.bypassDefaults.apply(d,[c])},deepExtend:function(b){var c=a.extend(!0,{},a.fn.wysihtml5.defaultOptions,b||{}),d=this;return e.bypassDefaults.apply(d,[c])},init:function(a){var b=this;return e.shallowExtend.apply(b,[a])}};a.fn.wysihtml5=function(b){return e[b]?e[b].apply(this,Array.prototype.slice.call(arguments,1)):"object"!=typeof b&&b?(a.error("Method "+b+" does not exist on jQuery.wysihtml5"),void 0):e.init.apply(this,arguments)},a.fn.wysihtml5.Constructor=d;var f=a.fn.wysihtml5.defaultOptions={"font-styles":!0,color:!1,emphasis:!0,lists:!0,html:!1,link:!0,image:!0,events:{},parserRules:{classes:{"wysiwyg-color-silver":1,"wysiwyg-color-gray":1,"wysiwyg-color-white":1,"wysiwyg-color-maroon":1,"wysiwyg-color-red":1,"wysiwyg-color-purple":1,"wysiwyg-color-fuchsia":1,"wysiwyg-color-green":1,"wysiwyg-color-lime":1,"wysiwyg-color-olive":1,"wysiwyg-color-yellow":1,"wysiwyg-color-navy":1,"wysiwyg-color-blue":1,"wysiwyg-color-teal":1,"wysiwyg-color-aqua":1,"wysiwyg-color-orange":1},tags:{b:{},i:{},strong:{},em:{},p:{},br:{},ol:{},ul:{},li:{},h1:{},h2:{},h3:{},h4:{},h5:{},h6:{},blockquote:{},u:1,img:{check_attributes:{width:"numbers",alt:"alt",src:"url",height:"numbers"}},a:{check_attributes:{href:"url"},set_attributes:{target:"_blank",rel:"nofollow"}},span:1,div:1,code:1,pre:1}},locale:"en"};"undefined"==typeof a.fn.wysihtml5.defaultOptionsCache&&(a.fn.wysihtml5.defaultOptionsCache=a.extend(!0,{},a.fn.wysihtml5.defaultOptions));var g=a.fn.wysihtml5.locale={}}(window.jQuery,window.wysihtml5),function(a){a.fn.wysihtml5.locale.en=a.fn.wysihtml5.locale["en-US"]={font_styles:{normal:"Normal text",h1:"Heading 1",h2:"Heading 2",h3:"Heading 3",h4:"Heading 4",h5:"Heading 5",h6:"Heading 6"},emphasis:{bold:"Bold",italic:"Italic",underline:"Underline"},lists:{unordered:"Unordered list",ordered:"Ordered list",outdent:"Outdent",indent:"Indent"},link:{insert:"Insert link",cancel:"Cancel",target:"Open link in new window"},image:{insert:"Insert image",cancel:"Cancel"},html:{edit:"Edit HTML"},colours:{black:"Black",silver:"Silver",gray:"Grey",maroon:"Maroon",red:"Red",purple:"Purple",green:"Green",olive:"Olive",navy:"Navy",blue:"Blue",orange:"Orange"}}}(jQuery);$(function() {
    "use strict";

    if ($("#page-js").text().indexOf("admin-console") > -1) {
    	
	    $(".delete-achievement-button").click(function(){
	    	var uuid = $(this).closest("tr").data("uuid");
	    	
	    	$.ajax({
	    		type: "POST",
	    		url: "/admin/achievement",
	    		data: "uuid="+uuid+"&action=delete"
	    	});
	    	
	    	$(this).closest("tr").remove();
	    });
	    
	    $(".delete-report-button").click(function(){
	    	var uuid = $(this).closest("tr").data("uuid");
	    	
	    	$.ajax({
	    		type: "POST",
	    		url: "/report/manage",
	    		data: "reportUuid="+uuid+"&action=delete-report"
	    	});
	    	
	    	$(this).closest("tr").remove();
	    });
	    
	    $(".delete-reported-content-button").click(function(){
	    	var uuid = $(this).closest("tr").data("uuid");
	    	
	    	$.ajax({
	    		type: "POST",
	    		url: "/report/manage",
	    		data: "reportUuid="+uuid+"&action=delete-content"
	    	});
	    	
	    	$(this).closest("tr").remove();
	    });
	    
	    $(".flag-reporter-button").click(function(){
	    	var uuid = $(this).closest("tr").data("uuid");
	    	
	    	$.ajax({
	    		type: "POST",
	    		url: "/report/manage",
	    		data: "reportUuid="+uuid+"&action=flag-reporter"
	    	});
	    	
	    	$(this).closest("tr").remove();
	    });
	    
    }
    
});/*!
 * Author: Abdullah A Almsaeed
 * Date: 4 Jan 2014
 * Description:
 *      This file should be included in all pages
 !**/

/*
 * Global variables. If you change any of these vars, don't forget 
 * to change the values in the less files!
 */
var left_side_width = 220; //Sidebar width in pixels

$(function() {
    "use strict";

    //Enable sidebar toggle
    $("[data-toggle='offcanvas']").click(function(e) {
        e.preventDefault();

        //If window is small enough, enable sidebar push menu
        if ($(window).width() <= 992) {
            $('.row-offcanvas').toggleClass('active');
            $('.left-side').removeClass("collapse-left");
            $(".right-side").removeClass("strech");
            $('.row-offcanvas').toggleClass("relative");
        } else {
            //Else, enable content streching
            $('.left-side').toggleClass("collapse-left");
            $(".right-side").toggleClass("strech");
        }
    });

    //Add hover support for touch devices
    $('.btn').bind('touchstart', function() {
        $(this).addClass('hover');
    }).bind('touchend', function() {
        $(this).removeClass('hover');
    });

    //Activate tooltips
    $("[data-toggle='tooltip']").tooltip();

    /*     
     * Add collapse and remove events to boxes
     */
    $("[data-widget='collapse']").click(function() {
        //Find the box parent        
        var box = $(this).parents(".box").first();
        //Find the body and the footer
        var bf = box.find(".box-body, .box-footer");
        if (!box.hasClass("collapsed-box")) {
            box.addClass("collapsed-box");
            //Convert minus into plus
            $(this).children(".fa-minus").removeClass("fa-minus").addClass("fa-plus");
            bf.slideUp();
        } else {
            box.removeClass("collapsed-box");
            //Convert plus into minus
            $(this).children(".fa-plus").removeClass("fa-plus").addClass("fa-minus");
            bf.slideDown();
        }
    });

    /*
     * ADD SLIMSCROLL TO THE TOP NAV DROPDOWNS
     * ---------------------------------------
     */
    $(".navbar .menu").slimscroll({
        height: "200px",
        alwaysVisible: false,
        size: "3px"
    }).css("width", "100%");

    /*
     * INITIALIZE BUTTON TOGGLE
     * ------------------------
     */
    $('.btn-group[data-toggle="btn-toggle"]').each(function() {
        var group = $(this);
        $(this).find(".btn").click(function(e) {
            group.find(".btn.active").removeClass("active");
            $(this).addClass("active");
            e.preventDefault();
        });

    });

    $("[data-widget='remove']").click(function() {
        //Find the box parent        
        var box = $(this).parents(".box").first();
        box.slideUp();
    });

    /* Sidebar tree view */
    $(".sidebar .treeview").tree();

    /*
	* Make sure that the sidebar is streched full height
	* ---------------------------------------------
	* We are gonna assign a min-height value every time the
	* wrapper gets resized and upon page load. We will use
	* Ben Alman's method for detecting the resize event.
	*
	**/
    function _fix() {
		//Get window height and the wrapper height
		var neg = $('.main-header').height() - ($('.main-footer').height());				
		var window_height = $(window).height()
		var sidebar_height = $(".left-side").height();
		if (window_height >= sidebar_height) {
				$(".content, .left-side").css('min-height', window_height - neg);
		} else {
				$(".content, .left-side").css('min-height', sidebar_height - neg);
		}
    }
    //Fire upon load
    _fix();
    //Fire when wrapper is resized
    $(window).resize(function(){
    	window.setTimeout(function() {
    		_fix();
    		fix_sidebar();
    	}, 1000);
    });

    //Fix the fixed layout sidebar scroll bug
    fix_sidebar();

    /*
     * We are gonna initialize all checkbox and radio inputs to 
     * iCheck plugin in.
     * You can find the documentation at http://fronteed.com/iCheck/
     */
    $("input[type='checkbox']:not(.simple), input[type='radio']:not(.simple)").iCheck({
        checkboxClass: 'icheckbox_minimal',
        radioClass: 'iradio_minimal'
    });
    
    
    $(".login-button").click(function(){
    	$("#login-modal").removeClass("hidden");
    });
    
    $(".login-action").click(function(){
    	$("#login-modal").addClass("hidden");
    });
    
    $("#close-ad").click(function(){
    	$(this).closest(".ad").remove();
    });
    
    $(".wysihtml5-beam-me-up").wysihtml5();
    
    $(".nav .dropdown .dropdown-toggle").click(function(){
    	var type = $(this).data("type");
    	var userId = $(this).data("user");
    	var label = $(".label", this);
    	var num = parseInt(label.text());
    	
    	if (num > 0) {
    		$.ajax({
    			type: "POST",
    			url: "/mark-menu-items-read",
    			data: "userId="+userId+"&type=" + type
    		});
    	}
    	label.fadeOut().text("0");
    });
    
    $(".knob").knob();
    
    var affirmConsentCheckBox =  $(".affirm-consent-check-box");
    var affirmConsentButton = $(".affirm-consent-button");
    
    $(affirmConsentCheckBox).click(function(){
    	$(this).toggleClass("checked");
    	$(affirmConsentButton).toggleClass("disabled");
    });
    
	$("#affirm-consent-modal").each(function(){
    	var modal = this;
    	
    	$(affirmConsentButton).click(function(){
    		
    		var checked = $(affirmConsentCheckBox).hasClass("checked");
    		
    		if (checked) {
    			var userId = $(this).data("user");
    			$.ajax({
    				type: "POST",
    				url: "/affirm-consent",
    				data: "userId="+userId
    			});
    			$(modal).remove();
    		} else {
    			var style = {"border": "1px solid #f56954"}
    			$(affirmConsentCheckbox).css(style);
    		} 
    	});
    });
	
	var ratingInstructions = $(".rating-system-horizontal .rating-instructions");
	var ratingInstructionsButton = $(".info-button", ratingInstructions);
	
	$(ratingInstructionsButton).click(function(){
		$(ratingInstructions).toggleClass("no-height");
	});
    
});

function fix_sidebar() {
    //Make sure the body tag has the .fixed class
    if (!$("body").hasClass("fixed")) {
        return;
    }
    
    var style = {
        "height": ($(window).height() - $(".header").height()) + "px"
    }

    //Add slimscroll
    $(".sidebar").slimscroll({
        height: ($(window).height() - $(".header").height()) + "px",
        color: "rgba(0,0,0,0.2)"
    });
    
    $(".sidebar").parent().css(style)
    $(".sidebar").css(style);   
}

/*END DEMO*/
$(window).load(function() {
    /*! pace 0.4.17 */
    (function() {
        var a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V = [].slice, W = {}.hasOwnProperty, X = function(a, b) {
            function c() {
                this.constructor = a
            }
            for (var d in b)
                W.call(b, d) && (a[d] = b[d]);
            return c.prototype = b.prototype, a.prototype = new c, a.__super__ = b.prototype, a
        }, Y = [].indexOf || function(a) {
            for (var b = 0, c = this.length; c > b; b++)
                if (b in this && this[b] === a)
                    return b;
            return-1
        };
        for (t = {catchupTime:500, initialRate:.03, minTime:500, ghostTime:500, maxProgressPerFrame:10, easeFactor:1.25, startOnPageLoad:!0, restartOnPushState:!0, restartOnRequestAfter:500, target:"body", elements:{checkInterval:100, selectors:["body"]}, eventLag:{minSamples:10, sampleCount:3, lagThreshold:3}, ajax:{trackMethods:["GET"], trackWebSockets:!1}}, B = function() {
            var a;
            return null != (a = "undefined" != typeof performance && null !== performance ? "function" == typeof performance.now ? performance.now() : void 0 : void 0) ? a : +new Date
        }, D = window.requestAnimationFrame || window.mozRequestAnimationFrame || window.webkitRequestAnimationFrame || window.msRequestAnimationFrame, s = window.cancelAnimationFrame || window.mozCancelAnimationFrame, null == D && (D = function(a) {
            return setTimeout(a, 50)
        }, s = function(a) {
            return clearTimeout(a)
        }), F = function(a) {
            var b, c;
            return b = B(), (c = function() {
                var d;
                return d = B() - b, d >= 33 ? (b = B(), a(d, function() {
                    return D(c)
                })) : setTimeout(c, 33 - d)
            })()
        }, E = function() {
            var a, b, c;
            return c = arguments[0], b = arguments[1], a = 3 <= arguments.length ? V.call(arguments, 2) : [], "function" == typeof c[b] ? c[b].apply(c, a) : c[b]
        }, u = function() {
            var a, b, c, d, e, f, g;
            for (b = arguments[0], d = 2 <= arguments.length?V.call(arguments, 1):[], f = 0, g = d.length; g > f; f++)
                if (c = d[f])
                    for (a in c)
                        W.call(c, a) && (e = c[a], null != b[a] && "object" == typeof b[a] && null != e && "object" == typeof e ? u(b[a], e) : b[a] = e);
            return b
        }, p = function(a) {
            var b, c, d, e, f;
            for (c = b = 0, e = 0, f = a.length; f > e; e++)
                d = a[e], c += Math.abs(d), b++;
            return c / b
        }, w = function(a, b) {
            var c, d, e;
            if (null == a && (a = "options"), null == b && (b = !0), e = document.querySelector("[data-pace-" + a + "]")) {
                if (c = e.getAttribute("data-pace-" + a), !b)
                    return c;
                try {
                    return JSON.parse(c)
                } catch (f) {
                    return d = f, "undefined" != typeof console && null !== console ? console.error("Error parsing inline pace options", d) : void 0
                }
            }
        }, g = function() {
            function a() {
            }
            return a.prototype.on = function(a, b, c, d) {
                var e;
                return null == d && (d = !1), null == this.bindings && (this.bindings = {}), null == (e = this.bindings)[a] && (e[a] = []), this.bindings[a].push({handler: b, ctx: c, once: d})
            }, a.prototype.once = function(a, b, c) {
                return this.on(a, b, c, !0)
            }, a.prototype.off = function(a, b) {
                var c, d, e;
                if (null != (null != (d = this.bindings) ? d[a] : void 0)) {
                    if (null == b)
                        return delete this.bindings[a];
                    for (c = 0, e = []; c < this.bindings[a].length; )
                        this.bindings[a][c].handler === b ? e.push(this.bindings[a].splice(c, 1)) : e.push(c++);
                    return e
                }
            }, a.prototype.trigger = function() {
                var a, b, c, d, e, f, g, h, i;
                if (c = arguments[0], a = 2 <= arguments.length ? V.call(arguments, 1) : [], null != (g = this.bindings) ? g[c] : void 0) {
                    for (e = 0, i = []; e < this.bindings[c].length; )
                        h = this.bindings[c][e], d = h.handler, b = h.ctx, f = h.once, d.apply(null != b ? b : this, a), f ? i.push(this.bindings[c].splice(e, 1)) : i.push(e++);
                    return i
                }
            }, a
        }(), null == window.Pace && (window.Pace = {}), u(Pace, g.prototype), C = Pace.options = u({}, t, window.paceOptions, w()), S = ["ajax", "document", "eventLag", "elements"], O = 0, Q = S.length; Q > O; O++)
            I = S[O], C[I] === !0 && (C[I] = t[I]);
        i = function(a) {
            function b() {
                return T = b.__super__.constructor.apply(this, arguments)
            }
            return X(b, a), b
        }(Error), b = function() {
            function a() {
                this.progress = 0
            }
            return a.prototype.getElement = function() {
                var a;
                if (null == this.el) {
                    if (a = document.querySelector(C.target), !a)
                        throw new i;
                    this.el = document.createElement("div"), this.el.className = "pace pace-active", document.body.className = document.body.className.replace("pace-done", ""), document.body.className += " pace-running", this.el.innerHTML = '<div class="pace-progress">\n  <div class="pace-progress-inner"></div>\n</div>\n<div class="pace-activity"></div>', null != a.firstChild ? a.insertBefore(this.el, a.firstChild) : a.appendChild(this.el)
                }
                return this.el
            }, a.prototype.finish = function() {
                var a;
                return a = this.getElement(), a.className = a.className.replace("pace-active", ""), a.className += " pace-inactive", document.body.className = document.body.className.replace("pace-running", ""), document.body.className += " pace-done"
            }, a.prototype.update = function(a) {
                return this.progress = a, this.render()
            }, a.prototype.destroy = function() {
                try {
                    this.getElement().parentNode.removeChild(this.getElement())
                } catch (a) {
                    i = a
                }
                return this.el = void 0
            }, a.prototype.render = function() {
                var a, b;
                return null == document.querySelector(C.target) ? !1 : (a = this.getElement(), a.children[0].style.width = "" + this.progress + "%", (!this.lastRenderedProgress || this.lastRenderedProgress | 0 !== this.progress | 0) && (a.children[0].setAttribute("data-progress-text", "" + (0 | this.progress) + "%"), this.progress >= 100 ? b = "99" : (b = this.progress < 10 ? "0" : "", b += 0 | this.progress), a.children[0].setAttribute("data-progress", "" + b)), this.lastRenderedProgress = this.progress)
            }, a.prototype.done = function() {
                return this.progress >= 100
            }, a
        }(), h = function() {
            function a() {
                this.bindings = {}
            }
            return a.prototype.trigger = function(a, b) {
                var c, d, e, f, g;
                if (null != this.bindings[a]) {
                    for (f = this.bindings[a], g = [], d = 0, e = f.length; e > d; d++)
                        c = f[d], g.push(c.call(this, b));
                    return g
                }
            }, a.prototype.on = function(a, b) {
                var c;
                return null == (c = this.bindings)[a] && (c[a] = []), this.bindings[a].push(b)
            }, a
        }(), N = window.XMLHttpRequest, M = window.XDomainRequest, L = window.WebSocket, v = function(a, b) {
            var c, d, e, f;
            f = [];
            for (d in b.prototype)
                try {
                    e = b.prototype[d], null == a[d] && "function" != typeof e ? f.push(a[d] = e) : f.push(void 0)
                } catch (g) {
                    c = g
                }
            return f
        }, z = [], Pace.ignore = function() {
            var a, b, c;
            return b = arguments[0], a = 2 <= arguments.length ? V.call(arguments, 1) : [], z.unshift("ignore"), c = b.apply(null, a), z.shift(), c
        }, Pace.track = function() {
            var a, b, c;
            return b = arguments[0], a = 2 <= arguments.length ? V.call(arguments, 1) : [], z.unshift("track"), c = b.apply(null, a), z.shift(), c
        }, H = function(a) {
            var b;
            if (null == a && (a = "GET"), "track" === z[0])
                return"force";
            if (!z.length && C.ajax) {
                if ("socket" === a && C.ajax.trackWebSockets)
                    return!0;
                if (b = a.toUpperCase(), Y.call(C.ajax.trackMethods, b) >= 0)
                    return!0
            }
            return!1
        }, j = function(a) {
            function b() {
                var a, c = this;
                b.__super__.constructor.apply(this, arguments), a = function(a) {
                    var b;
                    return b = a.open, a.open = function(d, e) {
                        return H(d) && c.trigger("request", {type: d, url: e, request: a}), b.apply(a, arguments)
                    }
                }, window.XMLHttpRequest = function(b) {
                    var c;
                    return c = new N(b), a(c), c
                }, v(window.XMLHttpRequest, N), null != M && (window.XDomainRequest = function() {
                    var b;
                    return b = new M, a(b), b
                }, v(window.XDomainRequest, M)), null != L && C.ajax.trackWebSockets && (window.WebSocket = function(a, b) {
                    var d;
                    return d = new L(a, b), H("socket") && c.trigger("request", {type: "socket", url: a, protocols: b, request: d}), d
                }, v(window.WebSocket, L))
            }
            return X(b, a), b
        }(h), P = null, x = function() {
            return null == P && (P = new j), P
        }, x().on("request", function(b) {
            var c, d, e, f;
            return f = b.type, e = b.request, Pace.running || C.restartOnRequestAfter === !1 && "force" !== H(f) ? void 0 : (d = arguments, c = C.restartOnRequestAfter || 0, "boolean" == typeof c && (c = 0), setTimeout(function() {
                var b, c, g, h, i, j;
                if (b = "socket" === f ? e.readyState < 2 : 0 < (h = e.readyState) && 4 > h) {
                    for (Pace.restart(), i = Pace.sources, j = [], c = 0, g = i.length; g > c; c++) {
                        if (I = i[c], I instanceof a) {
                            I.watch.apply(I, d);
                            break
                        }
                        j.push(void 0)
                    }
                    return j
                }
            }, c))
        }), a = function() {
            function a() {
                var a = this;
                this.elements = [], x().on("request", function() {
                    return a.watch.apply(a, arguments)
                })
            }
            return a.prototype.watch = function(a) {
                var b, c, d;
                return d = a.type, b = a.request, c = "socket" === d ? new m(b) : new n(b), this.elements.push(c)
            }, a
        }(), n = function() {
            function a(a) {
                var b, c, d, e, f, g, h = this;
                if (this.progress = 0, null != window.ProgressEvent)
                    for (c = null, a.addEventListener("progress", function(a) {
                        return h.progress = a.lengthComputable ? 100 * a.loaded / a.total : h.progress + (100 - h.progress) / 2
                    }), g = ["load", "abort", "timeout", "error"], d = 0, e = g.length; e > d; d++)
                        b = g[d], a.addEventListener(b, function() {
                            return h.progress = 100
                        });
                else
                    f = a.onreadystatechange, a.onreadystatechange = function() {
                        var b;
                        return 0 === (b = a.readyState) || 4 === b ? h.progress = 100 : 3 === a.readyState && (h.progress = 50), "function" == typeof f ? f.apply(null, arguments) : void 0
                    }
            }
            return a
        }(), m = function() {
            function a(a) {
                var b, c, d, e, f = this;
                for (this.progress = 0, e = ["error", "open"], c = 0, d = e.length; d > c; c++)
                    b = e[c], a.addEventListener(b, function() {
                        return f.progress = 100
                    })
            }
            return a
        }(), d = function() {
            function a(a) {
                var b, c, d, f;
                for (null == a && (a = {}), this.elements = [], null == a.selectors && (a.selectors = []), f = a.selectors, c = 0, d = f.length; d > c; c++)
                    b = f[c], this.elements.push(new e(b))
            }
            return a
        }(), e = function() {
            function a(a) {
                this.selector = a, this.progress = 0, this.check()
            }
            return a.prototype.check = function() {
                var a = this;
                return document.querySelector(this.selector) ? this.done() : setTimeout(function() {
                    return a.check()
                }, C.elements.checkInterval)
            }, a.prototype.done = function() {
                return this.progress = 100
            }, a
        }(), c = function() {
            function a() {
                var a, b, c = this;
                this.progress = null != (b = this.states[document.readyState]) ? b : 100, a = document.onreadystatechange, document.onreadystatechange = function() {
                    return null != c.states[document.readyState] && (c.progress = c.states[document.readyState]), "function" == typeof a ? a.apply(null, arguments) : void 0
                }
            }
            return a.prototype.states = {loading: 0, interactive: 50, complete: 100}, a
        }(), f = function() {
            function a() {
                var a, b, c, d, e, f = this;
                this.progress = 0, a = 0, e = [], d = 0, c = B(), b = setInterval(function() {
                    var g;
                    return g = B() - c - 50, c = B(), e.push(g), e.length > C.eventLag.sampleCount && e.shift(), a = p(e), ++d >= C.eventLag.minSamples && a < C.eventLag.lagThreshold ? (f.progress = 100, clearInterval(b)) : f.progress = 100 * (3 / (a + 3))
                }, 50)
            }
            return a
        }(), l = function() {
            function a(a) {
                this.source = a, this.last = this.sinceLastUpdate = 0, this.rate = C.initialRate, this.catchup = 0, this.progress = this.lastProgress = 0, null != this.source && (this.progress = E(this.source, "progress"))
            }
            return a.prototype.tick = function(a, b) {
                var c;
                return null == b && (b = E(this.source, "progress")), b >= 100 && (this.done = !0), b === this.last ? this.sinceLastUpdate += a : (this.sinceLastUpdate && (this.rate = (b - this.last) / this.sinceLastUpdate), this.catchup = (b - this.progress) / C.catchupTime, this.sinceLastUpdate = 0, this.last = b), b > this.progress && (this.progress += this.catchup * a), c = 1 - Math.pow(this.progress / 100, C.easeFactor), this.progress += c * this.rate * a, this.progress = Math.min(this.lastProgress + C.maxProgressPerFrame, this.progress), this.progress = Math.max(0, this.progress), this.progress = Math.min(100, this.progress), this.lastProgress = this.progress, this.progress
            }, a
        }(), J = null, G = null, q = null, K = null, o = null, r = null, Pace.running = !1, y = function() {
            return C.restartOnPushState ? Pace.restart() : void 0
        }, null != window.history.pushState && (R = window.history.pushState, window.history.pushState = function() {
            return y(), R.apply(window.history, arguments)
        }), null != window.history.replaceState && (U = window.history.replaceState, window.history.replaceState = function() {
            return y(), U.apply(window.history, arguments)
        }), k = {ajax: a, elements: d, document: c, eventLag: f}, (A = function() {
            var a, c, d, e, f, g, h, i;
            for (Pace.sources = J = [], g = ["ajax", "elements", "document", "eventLag"], c = 0, e = g.length; e > c; c++)
                a = g[c], C[a] !== !1 && J.push(new k[a](C[a]));
            for (i = null != (h = C.extraSources)?h:[], d = 0, f = i.length; f > d; d++)
                I = i[d], J.push(new I(C));
            return Pace.bar = q = new b, G = [], K = new l
        })(), Pace.stop = function() {
            return Pace.trigger("stop"), Pace.running = !1, q.destroy(), r = !0, null != o && ("function" == typeof s && s(o), o = null), A()
        }, Pace.restart = function() {
            return Pace.trigger("restart"), Pace.stop(), Pace.start()
        }, Pace.go = function() {
            return Pace.running = !0, q.render(), r = !1, o = F(function(a, b) {
                var c, d, e, f, g, h, i, j, k, m, n, o, p, s, t, u, v;
                for (j = 100 - q.progress, d = o = 0, e = !0, h = p = 0, t = J.length; t > p; h = ++p)
                    for (I = J[h], m = null != G[h]?G[h]:G[h] = [], g = null != (v = I.elements)?v:[I], i = s = 0, u = g.length; u > s; i = ++s)
                        f = g[i], k = null != m[i] ? m[i] : m[i] = new l(f), e &= k.done, k.done || (d++, o += k.tick(a));
                return c = o / d, q.update(K.tick(a, c)), n = B(), q.done() || e || r ? (q.update(100), Pace.trigger("done"), setTimeout(function() {
                    return q.finish(), Pace.running = !1, Pace.trigger("hide")
                }, Math.max(C.ghostTime, Math.min(C.minTime, B() - n)))) : b()
            })
        }, Pace.start = function(a) {
            u(C, a), Pace.running = !0;
            try {
                q.render()
            } catch (b) {
                i = b
            }
            return document.querySelector(".pace") ? (Pace.trigger("start"), Pace.go()) : setTimeout(Pace.start, 50)
        }, "function" == typeof define && define.amd ? define('theme-app', [], function() {
            return Pace
        }) : "object" == typeof exports ? module.exports = Pace : C.startOnPageLoad && Pace.start()
    }).call(this);
});

/* 
 * BOX REFRESH BUTTON 
 * ------------------
 * This is a custom plugin to use with the compenet BOX. It allows you to add
 * a refresh button to the box. It converts the box's state to a loading state.
 * 
 * USAGE:
 *  $("#box-widget").boxRefresh( options );
 * */
(function($) {
    "use strict";

    $.fn.boxRefresh = function(options) {

        // Render options
        var settings = $.extend({
            //Refressh button selector
            trigger: ".refresh-btn",
            //File source to be loaded (e.g: ajax/src.php)
            source: "",
            //Callbacks
            onLoadStart: function(box) {
            }, //Right after the button has been clicked
            onLoadDone: function(box) {
            } //When the source has been loaded

        }, options);

        //The overlay
        var overlay = $('<div class="overlay"></div><div class="loading-img"></div>');

        return this.each(function() {
            //if a source is specified
            if (settings.source === "") {
                if (console) {
                    console.log("Please specify a source first - boxRefresh()");
                }
                return;
            }
            //the box
            var box = $(this);
            //the button
            var rBtn = box.find(settings.trigger).first();

            //On trigger click
            rBtn.click(function(e) {
                e.preventDefault();
                //Add loading overlay
                start(box);

                //Perform ajax call
                box.find(".box-body").load(settings.source, function() {
                    done(box);
                });


            });

        });

        function start(box) {
            //Add overlay and loading img
            box.append(overlay);

            settings.onLoadStart.call(box);
        }

        function done(box) {
            //Remove overlay and loading img
            box.find(overlay).remove();

            settings.onLoadDone.call(box);
        }

    };

})(jQuery);

/*
 * SIDEBAR MENU
 * ------------
 * This is a custom plugin for the sidebar menu. It provides a tree view.
 * 
 * Usage:
 * $(".sidebar).tree();
 * 
 * Note: This plugin does not accept any options. Instead, it only requires a class
 *       added to the element that contains a sub-menu.
 *       
 * When used with the sidebar, for example, it would look something like this:
 * <ul class='sidebar-menu'>
 *      <li class="treeview active">
 *          <a href="#>Menu</a>
 *          <ul class='treeview-menu'>
 *              <li class='active'><a href=#>Level 1</a></li>
 *          </ul>
 *      </li>
 * </ul>
 * 
 * Add .active class to <li> elements if you want the menu to be open automatically
 * on page load. See above for an example.
 */
(function($) {
    "use strict";

    $.fn.tree = function() {

        return this.each(function() {
            var btn = $(this).children("a").first();
            var menu = $(this).children(".treeview-menu").first();
            var isActive = $(this).hasClass('active');

            //initialize already active menus
            if (isActive) {
                menu.show();
                btn.children(".fa-angle-right").first().removeClass("fa-angle-right").addClass("fa-angle-down");
            }
            //Slide open or close the menu on link click
            btn.click(function(e) {
                e.preventDefault();
                if (isActive) {
                    //Slide up to close menu
                    menu.slideUp();
                    isActive = false;
                    btn.children(".fa-angle-down").first().removeClass("fa-angle-down").addClass("fa-angle-right");
                    btn.parent("li").removeClass("active");
                } else {
                    //Slide down to open menu
                    menu.slideDown();
                    isActive = true;
                    btn.children(".fa-angle-right").first().removeClass("fa-angle-right").addClass("fa-angle-down");
                    btn.parent("li").addClass("active");
                }
            });

            /* Add margins to submenu elements to give it a tree look */
            menu.find("li > a").each(function() {
                var pad = parseInt($(this).css("margin-left")) + 10;

                $(this).css({"margin-left": pad + "px"});
            });

        });

    };


}(jQuery));

/*
 * TODO LIST CUSTOM PLUGIN
 * -----------------------
 * This plugin depends on iCheck plugin for checkbox and radio inputs
 */
(function($) {
    "use strict";

    $.fn.todolist = function(options) {
        // Render options
        var settings = $.extend({
            //When the user checks the input
            onCheck: function(ele) {
            },
            //When the user unchecks the input
            onUncheck: function(ele) {
            }
        }, options);

        return this.each(function() {
            $('input', this).on('ifChecked', function(event) {
                var ele = $(this).parents("li").first();
                ele.toggleClass("done");
                settings.onCheck.call(ele);
            });

            $('input', this).on('ifUnchecked', function(event) {
                var ele = $(this).parents("li").first();
                ele.toggleClass("done");
                settings.onUncheck.call(ele);
            });
        });
    };

}(jQuery));

/* CENTER ELEMENTS */
(function($) {
    "use strict";
    jQuery.fn.center = function(parent) {
        if (parent) {
            parent = this.parent();
        } else {
            parent = window;
        }
        this.css({
            "position": "absolute",
            "top": ((($(parent).height() - this.outerHeight()) / 2) + $(parent).scrollTop() + "px"),
            "left": ((($(parent).width() - this.outerWidth()) / 2) + $(parent).scrollLeft() + "px")
        });
        return this;
    }
}(jQuery));

/*
 * jQuery resize event - v1.1 - 3/14/2010
 * http://benalman.com/projects/jquery-resize-plugin/
 * 
 * Copyright (c) 2010 "Cowboy" Ben Alman
 * Dual licensed under the MIT and GPL licenses.
 * http://benalman.com/about/license/
 */
(function($, h, c) {
    var a = $([]), e = $.resize = $.extend($.resize, {}), i, k = "setTimeout", j = "resize", d = j + "-special-event", b = "delay", f = "throttleWindow";
    e[b] = 250;
    e[f] = true;
    $.event.special[j] = {setup: function() {
            if (!e[f] && this[k]) {
                return false;
            }
            var l = $(this);
            a = a.add(l);
            $.data(this, d, {w: l.width(), h: l.height()});
            if (a.length === 1) {
                g();
            }
        }, teardown: function() {
            if (!e[f] && this[k]) {
                return false
            }
            var l = $(this);
            a = a.not(l);
            l.removeData(d);
            if (!a.length) {
                clearTimeout(i);
            }
        }, add: function(l) {
            if (!e[f] && this[k]) {
                return false
            }
            var n;
            function m(s, o, p) {
                var q = $(this), r = $.data(this, d);
                r.w = o !== c ? o : q.width();
                r.h = p !== c ? p : q.height();
                n.apply(this, arguments)
            }
            if ($.isFunction(l)) {
                n = l;
                return m
            } else {
                n = l.handler;
                l.handler = m
            }
        }};
    function g() {
        i = h[k](function() {
            a.each(function() {
                var n = $(this), m = n.width(), l = n.height(), o = $.data(this, d);
                if (m !== o.w || l !== o.h) {
                    n.trigger(j, [o.w = m, o.h = l])
                }
            });
            g()
        }, e[b])
    }}
)(jQuery, this);

/*!
 * SlimScroll https://github.com/rochal/jQuery-slimScroll
 * =======================================================
 * 
 * Copyright (c) 2011 Piotr Rochala (http://rocha.la) Dual licensed under the MIT 
 */
(function(f) {
    jQuery.fn.extend({slimScroll: function(h) {
            var a = f.extend({width: "auto", height: "250px", size: "7px", color: "#000", position: "right", distance: "1px", start: "top", opacity: 0.4, alwaysVisible: !1, disableFadeOut: !1, railVisible: !1, railColor: "#333", railOpacity: 0.2, railDraggable: !0, railClass: "slimScrollRail", barClass: "slimScrollBar", wrapperClass: "slimScrollDiv", allowPageScroll: !1, wheelStep: 20, touchScrollStep: 200, borderRadius: "0px", railBorderRadius: "0px"}, h);
            this.each(function() {
                function r(d) {
                    if (s) {
                        d = d ||
                                window.event;
                        var c = 0;
                        d.wheelDelta && (c = -d.wheelDelta / 120);
                        d.detail && (c = d.detail / 3);
                        f(d.target || d.srcTarget || d.srcElement).closest("." + a.wrapperClass).is(b.parent()) && m(c, !0);
                        d.preventDefault && !k && d.preventDefault();
                        k || (d.returnValue = !1)
                    }
                }
                function m(d, f, h) {
                    k = !1;
                    var e = d, g = b.outerHeight() - c.outerHeight();
                    f && (e = parseInt(c.css("top")) + d * parseInt(a.wheelStep) / 100 * c.outerHeight(), e = Math.min(Math.max(e, 0), g), e = 0 < d ? Math.ceil(e) : Math.floor(e), c.css({top: e + "px"}));
                    l = parseInt(c.css("top")) / (b.outerHeight() - c.outerHeight());
                    e = l * (b[0].scrollHeight - b.outerHeight());
                    h && (e = d, d = e / b[0].scrollHeight * b.outerHeight(), d = Math.min(Math.max(d, 0), g), c.css({top: d + "px"}));
                    b.scrollTop(e);
                    b.trigger("slimscrolling", ~~e);
                    v();
                    p()
                }
                function C() {
                    window.addEventListener ? (this.addEventListener("DOMMouseScroll", r, !1), this.addEventListener("mousewheel", r, !1), this.addEventListener("MozMousePixelScroll", r, !1)) : document.attachEvent("onmousewheel", r)
                }
                function w() {
                    u = Math.max(b.outerHeight() / b[0].scrollHeight * b.outerHeight(), D);
                    c.css({height: u + "px"});
                    var a = u == b.outerHeight() ? "none" : "block";
                    c.css({display: a})
                }
                function v() {
                    w();
                    clearTimeout(A);
                    l == ~~l ? (k = a.allowPageScroll, B != l && b.trigger("slimscroll", 0 == ~~l ? "top" : "bottom")) : k = !1;
                    B = l;
                    u >= b.outerHeight() ? k = !0 : (c.stop(!0, !0).fadeIn("fast"), a.railVisible && g.stop(!0, !0).fadeIn("fast"))
                }
                function p() {
                    a.alwaysVisible || (A = setTimeout(function() {
                        a.disableFadeOut && s || (x || y) || (c.fadeOut("slow"), g.fadeOut("slow"))
                    }, 1E3))
                }
                var s, x, y, A, z, u, l, B, D = 30, k = !1, b = f(this);
                if (b.parent().hasClass(a.wrapperClass)) {
                    var n = b.scrollTop(),
                            c = b.parent().find("." + a.barClass), g = b.parent().find("." + a.railClass);
                    w();
                    if (f.isPlainObject(h)) {
                        if ("height"in h && "auto" == h.height) {
                            b.parent().css("height", "auto");
                            b.css("height", "auto");
                            var q = b.parent().parent().height();
                            b.parent().css("height", q);
                            b.css("height", q)
                        }
                        if ("scrollTo"in h)
                            n = parseInt(a.scrollTo);
                        else if ("scrollBy"in h)
                            n += parseInt(a.scrollBy);
                        else if ("destroy"in h) {
                            c.remove();
                            g.remove();
                            b.unwrap();
                            return
                        }
                        m(n, !1, !0)
                    }
                } else {
                    a.height = "auto" == a.height ? b.parent().height() : a.height;
                    n = f("<div></div>").addClass(a.wrapperClass).css({position: "relative",
                        overflow: "hidden", width: a.width, height: a.height});
                    b.css({overflow: "hidden", width: a.width, height: a.height});
                    var g = f("<div></div>").addClass(a.railClass).css({width: a.size, height: "100%", position: "absolute", top: 0, display: a.alwaysVisible && a.railVisible ? "block" : "none", "border-radius": a.railBorderRadius, background: a.railColor, opacity: a.railOpacity, zIndex: 90}), c = f("<div></div>").addClass(a.barClass).css({background: a.color, width: a.size, position: "absolute", top: 0, opacity: a.opacity, display: a.alwaysVisible ?
                                "block" : "none", "border-radius": a.borderRadius, BorderRadius: a.borderRadius, MozBorderRadius: a.borderRadius, WebkitBorderRadius: a.borderRadius, zIndex: 99}), q = "right" == a.position ? {right: a.distance} : {left: a.distance};
                    g.css(q);
                    c.css(q);
                    b.wrap(n);
                    b.parent().append(c);
                    b.parent().append(g);
                    a.railDraggable && c.bind("mousedown", function(a) {
                        var b = f(document);
                        y = !0;
                        t = parseFloat(c.css("top"));
                        pageY = a.pageY;
                        b.bind("mousemove.slimscroll", function(a) {
                            currTop = t + a.pageY - pageY;
                            c.css("top", currTop);
                            m(0, c.position().top, !1)
                        });
                        b.bind("mouseup.slimscroll", function(a) {
                            y = !1;
                            p();
                            b.unbind(".slimscroll")
                        });
                        return!1
                    }).bind("selectstart.slimscroll", function(a) {
                        a.stopPropagation();
                        a.preventDefault();
                        return!1
                    });
                    g.hover(function() {
                        v()
                    }, function() {
                        p()
                    });
                    c.hover(function() {
                        x = !0
                    }, function() {
                        x = !1
                    });
                    b.hover(function() {
                        s = !0;
                        v();
                        p()
                    }, function() {
                        s = !1;
                        p()
                    });
                    b.bind("touchstart", function(a, b) {
                        a.originalEvent.touches.length && (z = a.originalEvent.touches[0].pageY)
                    });
                    b.bind("touchmove", function(b) {
                        k || b.originalEvent.preventDefault();
                        b.originalEvent.touches.length &&
                                (m((z - b.originalEvent.touches[0].pageY) / a.touchScrollStep, !0), z = b.originalEvent.touches[0].pageY)
                    });
                    w();
                    "bottom" === a.start ? (c.css({top: b.outerHeight() - c.outerHeight()}), m(0, !0)) : "top" !== a.start && (m(f(a.start).position().top, null, !0), a.alwaysVisible || c.hide());
                    C()
                }
            });
            return this
        }});
    jQuery.fn.extend({slimscroll: jQuery.fn.slimScroll})
})(jQuery);

/*! iCheck v1.0.1 by Damir Sultanov, http://git.io/arlzeA, MIT Licensed */
(function(h) {
    function F(a, b, d) {
        var c = a[0], e = /er/.test(d) ? m : /bl/.test(d) ? s : l, f = d == H ? {checked: c[l], disabled: c[s], indeterminate: "true" == a.attr(m) || "false" == a.attr(w)} : c[e];
        if (/^(ch|di|in)/.test(d) && !f)
            D(a, e);
        else if (/^(un|en|de)/.test(d) && f)
            t(a, e);
        else if (d == H)
            for (e in f)
                f[e] ? D(a, e, !0) : t(a, e, !0);
        else if (!b || "toggle" == d) {
            if (!b)
                a[p]("ifClicked");
            f ? c[n] !== u && t(a, e) : D(a, e)
        }
    }
    function D(a, b, d) {
        var c = a[0], e = a.parent(), f = b == l, A = b == m, B = b == s, K = A ? w : f ? E : "enabled", p = k(a, K + x(c[n])), N = k(a, b + x(c[n]));
        if (!0 !== c[b]) {
            if (!d &&
                    b == l && c[n] == u && c.name) {
                var C = a.closest("form"), r = 'input[name="' + c.name + '"]', r = C.length ? C.find(r) : h(r);
                r.each(function() {
                    this !== c && h(this).data(q) && t(h(this), b)
                })
            }
            A ? (c[b] = !0, c[l] && t(a, l, "force")) : (d || (c[b] = !0), f && c[m] && t(a, m, !1));
            L(a, f, b, d)
        }
        c[s] && k(a, y, !0) && e.find("." + I).css(y, "default");
        e[v](N || k(a, b) || "");
        B ? e.attr("aria-disabled", "true") : e.attr("aria-checked", A ? "mixed" : "true");
        e[z](p || k(a, K) || "")
    }
    function t(a, b, d) {
        var c = a[0], e = a.parent(), f = b == l, h = b == m, q = b == s, p = h ? w : f ? E : "enabled", t = k(a, p + x(c[n])),
                u = k(a, b + x(c[n]));
        if (!1 !== c[b]) {
            if (h || !d || "force" == d)
                c[b] = !1;
            L(a, f, p, d)
        }
        !c[s] && k(a, y, !0) && e.find("." + I).css(y, "pointer");
        e[z](u || k(a, b) || "");
        q ? e.attr("aria-disabled", "false") : e.attr("aria-checked", "false");
        e[v](t || k(a, p) || "")
    }
    function M(a, b) {
        if (a.data(q)) {
            a.parent().html(a.attr("style", a.data(q).s || ""));
            if (b)
                a[p](b);
            a.off(".i").unwrap();
            h(G + '[for="' + a[0].id + '"]').add(a.closest(G)).off(".i")
        }
    }
    function k(a, b, d) {
        if (a.data(q))
            return a.data(q).o[b + (d ? "" : "Class")]
    }
    function x(a) {
        return a.charAt(0).toUpperCase() +
                a.slice(1)
    }
    function L(a, b, d, c) {
        if (!c) {
            if (b)
                a[p]("ifToggled");
            a[p]("ifChanged")[p]("if" + x(d))
        }
    }
    var q = "iCheck", I = q + "-helper", u = "radio", l = "checked", E = "un" + l, s = "disabled", w = "determinate", m = "in" + w, H = "update", n = "type", v = "addClass", z = "removeClass", p = "trigger", G = "label", y = "cursor", J = /ipad|iphone|ipod|android|blackberry|windows phone|opera mini|silk/i.test(navigator.userAgent);
    h.fn[q] = function(a, b) {
        var d = 'input[type="checkbox"], input[type="' + u + '"]', c = h(), e = function(a) {
            a.each(function() {
                var a = h(this);
                c = a.is(d) ?
                        c.add(a) : c.add(a.find(d))
            })
        };
        if (/^(check|uncheck|toggle|indeterminate|determinate|disable|enable|update|destroy)$/i.test(a))
            return a = a.toLowerCase(), e(this), c.each(function() {
                var c = h(this);
                "destroy" == a ? M(c, "ifDestroyed") : F(c, !0, a);
                h.isFunction(b) && b()
            });
        if ("object" != typeof a && a)
            return this;
        var f = h.extend({checkedClass: l, disabledClass: s, indeterminateClass: m, labelHover: !0, aria: !1}, a), k = f.handle, B = f.hoverClass || "hover", x = f.focusClass || "focus", w = f.activeClass || "active", y = !!f.labelHover, C = f.labelHoverClass ||
                "hover", r = ("" + f.increaseArea).replace("%", "") | 0;
        if ("checkbox" == k || k == u)
            d = 'input[type="' + k + '"]';
        -50 > r && (r = -50);
        e(this);
        return c.each(function() {
            var a = h(this);
            M(a);
            var c = this, b = c.id, e = -r + "%", d = 100 + 2 * r + "%", d = {position: "absolute", top: e, left: e, display: "block", width: d, height: d, margin: 0, padding: 0, background: "#fff", border: 0, opacity: 0}, e = J ? {position: "absolute", visibility: "hidden"} : r ? d : {position: "absolute", opacity: 0}, k = "checkbox" == c[n] ? f.checkboxClass || "icheckbox" : f.radioClass || "i" + u, m = h(G + '[for="' + b + '"]').add(a.closest(G)),
                    A = !!f.aria, E = q + "-" + Math.random().toString(36).replace("0.", ""), g = '<div class="' + k + '" ' + (A ? 'role="' + c[n] + '" ' : "");
            m.length && A && m.each(function() {
                g += 'aria-labelledby="';
                this.id ? g += this.id : (this.id = E, g += E);
                g += '"'
            });
            g = a.wrap(g + "/>")[p]("ifCreated").parent().append(f.insert);
            d = h('<ins class="' + I + '"/>').css(d).appendTo(g);
            a.data(q, {o: f, s: a.attr("style")}).css(e);
            f.inheritClass && g[v](c.className || "");
            f.inheritID && b && g.attr("id", q + "-" + b);
            "static" == g.css("position") && g.css("position", "relative");
            F(a, !0, H);
            if (m.length)
                m.on("click.i mouseover.i mouseout.i touchbegin.i touchend.i", function(b) {
                    var d = b[n], e = h(this);
                    if (!c[s]) {
                        if ("click" == d) {
                            if (h(b.target).is("a"))
                                return;
                            F(a, !1, !0)
                        } else
                            y && (/ut|nd/.test(d) ? (g[z](B), e[z](C)) : (g[v](B), e[v](C)));
                        if (J)
                            b.stopPropagation();
                        else
                            return!1
                    }
                });
            a.on("click.i focus.i blur.i keyup.i keydown.i keypress.i", function(b) {
                var d = b[n];
                b = b.keyCode;
                if ("click" == d)
                    return!1;
                if ("keydown" == d && 32 == b)
                    return c[n] == u && c[l] || (c[l] ? t(a, l) : D(a, l)), !1;
                if ("keyup" == d && c[n] == u)
                    !c[l] && D(a, l);
                else if (/us|ur/.test(d))
                    g["blur" ==
                            d ? z : v](x)
            });
            d.on("click mousedown mouseup mouseover mouseout touchbegin.i touchend.i", function(b) {
                var d = b[n], e = /wn|up/.test(d) ? w : B;
                if (!c[s]) {
                    if ("click" == d)
                        F(a, !1, !0);
                    else {
                        if (/wn|er|in/.test(d))
                            g[v](e);
                        else
                            g[z](e + " " + w);
                        if (m.length && y && e == B)
                            m[/ut|nd/.test(d) ? z : v](C)
                    }
                    if (J)
                        b.stopPropagation();
                    else
                        return!1
                }
            })
        })
    }
})(window.jQuery || window.Zepto);
$(function() {
    "use strict";

    if ($("#page-js").text().indexOf("baseline-landing") > -1) {
    	$("#what-to-expect-button").click(function(){
    		$("#what-to-expect-text").toggleClass("hidden");
    		if (! $("#about-the-science-text").hasClass("hidden")){
    			$("#about-the-science-text").addClass("hidden")
    		}
    	});
    	
    	$("#about-the-science-button").click(function(){
    		$("#about-the-science-text").toggleClass("hidden");
    		if (! $("#what-to-expect-text").hasClass("hidden")){
    			$("#what-to-expect-text").addClass("hidden")
    		}
    	});
    }
    
});$(function() {
	"use strict";
	
	 if ($("#page-js").text().indexOf("compare-content") > -1) {
    
	    var removePreference = function(){
	    	$(this).removeClass("btn-success").addClass("btn-default");
	    };
	    
	    var addPreference = function(){
	    	$(this).removeClass("btn-default").addClass("btn-success");
	    };
	    
	    
	    $(".toggle-preference-button").click(function(){
	    	$(".toggle-preference-button").each(removePreference);
	    	$(this).each(addPreference);
	    });
	  
	    $("#next-step-button").click(function(){
	    	var body = $(this).closest('.box-body');
	    	
	    	var charSelected = $(".btn-success", body).data("character");
	    	var userId = $(this).data("user");
	     	
	    	$.ajax({
	    		type: "POST",
	    		url: "/personalize/comparison",
	    		data: "userId="+userId+"&preferenceCharacter="+charSelected
	    	});
	    });
	
	}
});$(function(){
	
	"use strict";
	if ($("#page-js").text().indexOf("content-approval") > -1) {
		
		var allSidebarLis = $(".content-approval-sidebar li");
		
		$(allSidebarLis).click(function(){
			var id = $(this).attr('id');
			
			$(allSidebarLis).removeClass("selected");
			
			$(this).addClass("selected");
			
			$(".content-approval-content-display").addClass("hidden");
			
			var editingScreen = $("#" + id + "-viewer");
			
			if (! $(editingScreen).hasClass("wysihtml5-activated")){
				$(".wysihtml5-beam-me-up-later", editingScreen).wysihtml5();
			}
			
			$(editingScreen).removeClass("hidden").addClass("wysihtml5-activated");
		});
		
		var contentApprovalActionButtons = $(".content-approval-action-buttons");
		
		$(".content-approval-action-button").click(function(){
			var action = $(this).data("action");
			var contentType = $(this).data("type");
			var uuid = $(this).parent().data("uuid");
		
			if (action == "save"){
				var parent = $(this).closest(".content-approval-content-display");
				var title = $("[name=title]", parent).val();
				var body = $("[name=body]", parent).val();
				var solution = $("[name=solution]", parent).val();
				var tags = $("[name=tags]", parent).val();
				
				$.ajax({
					type: "POST",
					url: "/admin/content-approval",
					data: "uuid="+uuid+"&action="+action+"&title="+title+"&body="+body+"&solution="+solution+"&tags="+tags
				});
				
			} else {
				$.ajax({
					type: "POST",
					url: "/admin/content-approval",
					data: "uuid="+uuid+"&action="+action+"&contentType="+contentType
				});
			}
			
			var color = "";
			if ($(this).hasClass("btn-success")){
				color = "green";
			} else if ($(this).hasClass("btn-warning")){
				color = "yellow";
			} else if ($(this).hasClass("btn-primary")){
				color = "light-blue";
			} else if ($(this).hasClass("btn-info")){
				color = "aqua";
			} else if ($(this).hasClass("btn-danger")){
				color = "red";
			}
			
			$(this).closest(".content-approval-action-buttons").children("button")
				.removeClass().addClass("btn").addClass("btn-default").addClass("disabled");
			$(this).removeClass("btn-default").addClass("bg-" + color).addClass("disabled")
			
			var id = $(this).closest(".content-approval-content-display").attr("id");
			
			var newId = "#" + id.substring(0, id.length - 7);
			$(newId).addClass("bg-" + color);
		});
	}
	
});$(function() {
    "use strict";
    
    if ($("#page-js").text().indexOf("explore") > -1) {
    
	    $(".content-rotating").each(function () {
	    	var contentRotating = this;
	    	var buttonGroup = $('.btn-group', this);
	    	var defaultButton = $('.btn', buttonGroup);
	    	var i = 1;
	    	$(".row", this).each( function () {
	    		var panel = this;
	    		var id = $(this).attr('id');
	    		var newId = id + "-button";
	    		var newButton = $(defaultButton).clone().appendTo(buttonGroup);
	    		$(newButton).attr('id', newId).removeClass('hidden').text('' + i);
	    		i = i + 1;
	    		
	    		$(newButton).click( function () {
	    			$('.row', contentRotating).addClass("hidden");
	    			$(panel).removeClass("hidden");
	    			$(".btn", buttonGroup).removeClass('btn-primary').addClass('btn-default');
	    			$(this).addClass('btn-primary').removeClass('btn-default');
	    		});
	    	});
	    	$(defaultButton).remove();
	    });
	    
	    
	    var toggleBookmarkButton = function(){
	    	var userId = $(this).data("user");
			var content = $(this).data("content");
			var action = $(this).data("action");
			
	    	$.ajax({
				type: "POST",
				url: "/bookmark",
				data: "userId="+userId+"&contentUuid="+content+"&action="+action
			});
	    	
	    	if (action === "add") {
	    		$(this).data("action","remove");
	    		$('i', this).removeClass("fa-bookmark-o").addClass("fa-bookmark");
	    	} else {
	    		$(this).data("action","add");
	    		$('i', this).removeClass("fa-bookmark").addClass("fa-bookmark-o");
	    	}
	    		
	    };
		
		$(".toggle-bookmark-button").click(toggleBookmarkButton);
		
    }
});$(function(){
	
	 if ($("#page-js").text().indexOf("lpg") > -1) {
	
		var playgroundButton = "<li><button class=\"btn btn-default fa fa-magic lpg-toggle\" title=\"Insert Math Equations\" type=\"button\"></button></li>";
		
		$(".wysihtml5-toolbar").append(playgroundButton);
		
		$(".lpg-toggle").click(function(){
			$(".lpg-toggle").each(function(){
				if ($(this).hasClass("btn-default")){
					$(this).removeClass("btn-default").addClass("btn-success");
				} else if ($(this).hasClass("btn-success")){
					$(this).removeClass("btn-success").addClass("btn-default");
				}
			});
			
			var coordinates = $(this).offset();
			coordinates.left = coordinates.left + 45;
			$(".latex-playground").toggleClass("hidden").offset(coordinates);
		});
		
		$(".latex-playground").draggable();
		
		var inputBox = $(".lpg-input-box");
		
		var updateResultBox = function(newText){
			var lineChoice = $("#lpg-latex-line-select").val();
			if (lineChoice == "inline"){
				newText = "\\(" + newText + "\\)";
			} else {
				newText = "$$" + newText + "$$";
			}
			$("#lpg-result-box").empty().text(newText);
			MathJax.Hub.Queue(["Typeset",MathJax.Hub,"lpg-result-box"]);
		}
		
		$(".lpg-button").click(function(){
			var tex = $(this).data("tex");
			var newText = $(inputBox).val() + tex
			$(inputBox).val(newText);
			updateResultBox(newText);
		});
		
		$(".lpg-input-box").change(function(){
			var newText = $(this).val();
			updateResultBox(newText);
		});
		
		$(".lpg-insert-into-body").click(function(){
			var iframe = $("#body-editor").parent().find("iframe");
			var editor = $(".wysihtml5-editor", iframe.contents());
			var expression = $(".lpg-input-box").first().val();
			var originalText = $(editor).text();
			if (originalText == $("#body-editor").attr("placeholder")){
				originalText = "";
			}
			var lineChoice = $("#lpg-latex-line-select").val();
			if (lineChoice == "inline"){
				expression = " \\( " + expression + " \\) ";
			} else {
				expression = " $$ " + expression + " $$ ";
			}
			var newText = originalText + " " + expression;
			$(editor).text(newText);
		});
	
		$(".lpg-insert-into-solution").click(function(){
			var iframe = $("#solution-editor").parent().find("iframe");
			var editor = $(".wysihtml5-editor", iframe.contents());
			var expression = $(".lpg-input-box").first().val();
			var originalText = $(editor).text();
			if (originalText == $("#solution-editor").attr("placeholder")){
				originalText = "";
			}
			var lineChoice = $("#lpg-latex-line-select").val();
			if (lineChoice == "inline"){
				expression = " \\( " + expression + " \\) ";
			} else {
				expression = " $$ " + expression + " $$ ";
			}
			var newText = originalText + " " + expression;
			$(editor).text(newText);
		});
		
		$(".lpg-instructions-toggle").click(function(){
			if ($(this).hasClass("btn-default")){
				$(this).addClass("btn-primary").removeClass("btn-default");
				$(".lpg-instructions").removeClass("hidden");
			} else {
				$(this).addClass("btn-default").removeClass("btn-primary");
				$(".lpg-instructions").addClass("hidden");
			}
		});
	}
});$(function() {
    "use strict";
    
    if ($("#page-js").text().indexOf("personalize-difficulty") > -1) {
	    $(".btn-group.difficulty-rating-buttons").each(function(){
	    	
	    	var userId = $(this).data("user");
	    	var contentUuid = $(this).data("uuid");
	    	var buttonGroup = this;
	    	
	    	$("button", this).click(function(){
	        	/*
	        	var difficulty = $(this).data("difficulty");
	        	
	        	$.ajax({
	        		type: "POST",
	        		url: "/personalize/difficulty",
	        		data: "userId="+userId+"&contentUuid="+contentUuid+"&difficulty="+difficulty
	        	});
	        	*/
	        	$("button", buttonGroup).removeClass("btn-success").addClass("btn-default");
	        	$(this).removeClass("btn-default").addClass("btn-success");
	    	});
	    });
    }
});$(function() {
    "use strict";
    
    if ($("#page-js").text().indexOf("personalize-interests") > -1) { 
    
	    var toggleInterestOnClick = function(){
	    	var interest = $(this).data("interest");
	    	var userId = $(this).data("user");
	    	
	    	if ($(this).hasClass("btn-default")){
	    		$(this) .removeClass("btn-default")
	    				.addClass("btn-success");
	    	} else {
	    		$(this) .removeClass("btn-success")
						.addClass("btn-default");
	    	}
	    };
	    
	    $(".toggle-interest-button").click(toggleInterestOnClick);
	  
	    $("#next-step-button").click(function(){
	    	var userId = $(this).data("user");
	    	var allTags = [];
	    	$(".toggle-interest-button.btn-success").each(function(){
	    		var interest = $(this).data("interest");
	    		allTags.push(interest);
	    	});
	    	
	    	var toRemove = [];
	    	$(".original-interest").each(function(){
	    		if (! $(this).hasClass("btn-success")){
	    			var interest = $(this).data("interest");
	        		toRemove.push(interest);
	    		}
	    	});
	    	
	    	$.ajax({
	    		type: "POST",
	    		url: "/personalize/interests",
	    		data: "userId="+userId+"&add-interests="+allTags+"&remove-interests="+toRemove
	    	});
	    });
	    
    }
});$(function() {
	"use strict";

	if ($("#page-js").text().indexOf("practiceProblem") > -1) { 

		var removeBookmarkOnClick = function(){
			console.log("This shouldn't be run!");
		};

		var addBookmarkOnClick = function(){
			var userId = $(this).data("user");
			var content = $(this).data("content");
			var action = "add";

			$.ajax({
				type: "POST",
				url: "/bookmark",
				data: "userId="+userId+"&contentUuid="+content+"&action="+action
			});

			$(this).unbind();
			$("a", this).text("Un-Bookmark Problem");
			$(this).click(removeBookmarkOnClick);
		};

		removeBookmarkOnClick = function(){
			var userId = $(this).data("user");
			var content = $(this).data("content");
			var action = "remove";
			$.ajax({
				type: "POST",
				url: "/bookmark",
				data: "userId="+userId+"&contentUuid="+content+"&action="+action
			});
			$(this).unbind();
			$("a", this).text("Bookmark Problem");
			$(this).click(addBookmarkOnClick);
		}


		$(".add-bookmark-button").click(addBookmarkOnClick);

		$(".remove-bookmark-button").click(removeBookmarkOnClick);
		
		$(".peek-button").click(function(){
			var peekButton = this;
			var panel = $(this).closest(".panel");
			var body = $(".panel-collapse", panel);
			var style = {
					"display" : "block",
					"max-height"  : "50px",
					"overflow": "hidden"
			}
			$(".box-title > a", panel).click(function(){
				$(peekButton).remove();
				$(body).removeAttr("style");
				$(this).off();
			});
			var currentMax = 50;
			$(body).css(style);
			$(this).off();
			$(this).click(function(){
				currentMax = currentMax + 50;
				var height = {"max-height": currentMax + "px"};
				$(body).css(height);
				if ($(body).height() < currentMax){
					$(body).removeAttr("style").addClass("in");
					$(this).remove();
				}
			});
		});
	}
});$(function() {
    "use strict";

    if ($("#page-js").text().indexOf("practiceProblemCreation") > -1) { 
    
	    $("#tags-input").tagsInput({
	    	'width': '100%',
	    	'height': 'auto',
	    	'defaultText': 'To Add Tag, Press Enter',
	    	'removeWithBackspace': true,
	    });
    }
});$(function() {
    "use strict";

    if ($("#page-js").text().indexOf("private-profile") > -1) { 
    
	    $("#email-pref-submit").click(function(){
	    	$(this).attr('disabled', 'disabled');
	    	
	    	var parent = $(this).closest("form");
	    	
	    	var reply = $("#email-pref-reply", parent).val();
	    	var karma = $("#email-pref-karma", parent).val();
	    	var recommend = $("#email-pref-recommend", parent).val();
	    	var userId = $(parent).data("user");
	    	
	    	$.ajax({
	    		type: "POST",
	    		url: "/change-email-preferences",
	    		data: "userId="+userId+"&emailReply="+reply+"&emailKarma="+karma+"&emailRecommend="+recommend
	    	});
	    	
	    	$(this).val("Changed!").addClass("btn-success").removeClass("btn-primary");
	    });
	    
	    $("#change-username-button").click(function(){
	    	$(this).attr('disabled', 'disabled');
	    	
	    	var parent = $(this).closest("form");
	    	
	    	var newName = $("#edit-user-name", parent).val();
	    	var userId = $(parent).data("user");
	    	
	    	$.ajax({
	    		type: "POST",
	    		url: "/change-user-name",
	    		data: "userId="+userId+"&edit-username="+newName
	    	});
	    	
	    	$(this).val("Changed!").addClass("btn-success").removeClass("btn-primary");
	    });
	    
	    $(".remove-bookmark-button").click(function(){
	    	var userId = $(this).data("user");
	    	var content = $(this).data("content");
	    	var action = "remove";
	    	
	    	$.ajax({
	    		type: "POST",
	    		url: "/bookmark",
	    		data: "userId="+userId+"&contentUuid="+content+"&action="+action
	    	});
	    });
	    
	    $(".remove-notification-button").click(function(){
	    	var userId = $(this).data("user");
	    	var uuid = $(this).data("uuid");
	    	var action = "remove";
	    	
	    	$.ajax({
	    		type: "POST",
	    		url: "/remove-notification",
	    		data: "userId="+userId+"&uuid="+uuid
	    	});
	    });
    }
});$(function() {
    "use strict";
    
    if ($("#page-js").text().indexOf("publicProfile") > -1) { 
    
		var toggleBookmarkButton = function(){
	    	var userId = $(this).data("user");
			var content = $(this).data("content");
			var action = $(this).data("action");
			
	    	$.ajax({
				type: "POST",
				url: "/bookmark",
				data: "userId="+userId+"&contentUuid="+content+"&action="+action
			});
	    	
	    	if (action === "add") {
	    		$(this).data("action","remove");
	    		$('i', this).removeClass("fa-bookmark-o").addClass("fa-bookmark");
	    	} else {
	    		$(this).data("action","add");
	    		$('i', this).removeClass("fa-bookmark").addClass("fa-bookmark-o");
	    	}
	    		
	    };
		
		$(".toggle-bookmark-button").click(toggleBookmarkButton);
    }
});$(function() {
    "use strict";

    if ($("#page-js").text().indexOf("question") > -1) { 
    
	    var removeBookmarkOnClick = function(){
	    	console.log("This shouldn't be run!");
	    };
	    
	    var addBookmarkOnClick = function(){
	    	var userId = $(this).data("user");
	    	var content = $(this).data("content");
	    	var action = "add";
	
	    	$.ajax({
	    		type: "POST",
	    		url: "/bookmark",
	    		data: "userId="+userId+"&contentUuid="+content+"&action="+action
	    	});
	    	
	    	$(this).unbind();
	    	$("a", this).text("Un-Bookmark Problem");
	    	$(this).click(removeBookmarkOnClick);
	    };
	    
	    removeBookmarkOnClick = function(){
	    	var userId = $(this).data("user");
	    	var content = $(this).data("content");
	    	var action = "remove";
	    	$.ajax({
	    		type: "POST",
	    		url: "/bookmark",
	    		data: "userId="+userId+"&contentUuid="+content+"&action="+action
	    	});
	    	$(this).unbind();
	    	$("a", this).text("Bookmark Problem");
	    	$(this).click(addBookmarkOnClick);
	    }
	    
	    
	    $(".add-bookmark-button").click(addBookmarkOnClick);
	    
	    $(".remove-bookmark-button").click(removeBookmarkOnClick);
	    
    }
});$(function() {
    "use strict";
    
    if ($("#page-js").text().indexOf("rating-system") > -1) { 
    
	    $(".rating-system-horizontal").each(function(){
	    	var button = $(".submit-rating", this);
	    	var contentUuid = $(this).data("uuid");
	    	var userId = $(this).data("user");
	    	
	    	var helpfulness = $("[name=helpfulness]", this);
	    	var difficulty = $("[name=difficulty]", this);
	    	var quality = $("[name=quality]", this);
	    	
	    	$(button).click(function(){
	    		var h = $(helpfulness).val();
	    		var d = $(difficulty).val();
	    		var q = $(quality).val();
	    		
	    		$.ajax({
	        		type: "POST",
	        		url: "/rating/submit",
	        		data: "userId="+userId+"&contentUuid="+contentUuid+"&helpfulness="+h+"&difficulty="+d+"&quality="+q,
	        		success: function(data){
	        			$(button).removeClass("btn-default").addClass("btn-success").text("Successfully Rated").addClass("disabled").unbind();
	        			$(helpfulness).trigger('configure', {"readOnly": true});
	        			$(difficulty).trigger('configure', {"readOnly": true});
	        			$(quality).trigger('configure', {"readOnly": true});
	        			
	        		}
	        	});
	    	});
	    });
	    
    }
});$(function() {
	"use strict";
	
	if ($("#page-js").text().indexOf("recommendations") > -1) { 
	
		var toggleBookmarkButton = function(){
	    	var userId = $(this).data("user");
			var content = $(this).data("content");
			var action = $(this).data("action");
			
	    	$.ajax({
				type: "POST",
				url: "/bookmark",
				data: "userId="+userId+"&contentUuid="+content+"&action="+action
			});
	    	
	    	if (action === "add") {
	    		$(this).data("action","remove");
	    		$(".fa", this).removeClass("fa-bookmark-o").addClass("fa-bookmark");
	    	} else {
	    		$(this).data("action","add");
	    		$(".fa", this).removeClass("fa-bookmark").addClass("fa-bookmark-o");
	    	}
	    };
		
		$(".toggle-bookmark-button").click(toggleBookmarkButton);
		
		var hideRecommendedContent = function(){
	    	var userId = $(this).data("user");
			var content = $(this).data("content");
			var action = $(this).data("action");
			
	    	$.ajax({
				type: "POST",
				url: "/recommendations",
				data: "userId="+userId+"&contentUuid="+content+"&action="+action
			});
	    	
	    	$(this).addClass('hovered');
	    	
	    	var fading = $(this).parent(".alert").addClass("fading");
	    	setTimeout(function () {$(fading).remove();}, 1200);
	    	
	    };
		
	    $(".hide-recommendation-button").click(hideRecommendedContent);
	    
	    var disinterestedContent = function () {
	    	var notYetSent = $(".fa", this).hasClass("fa-thumbs-o-down");
	    	var alreadySent = $(".fa", this).hasClass("fa-thumbs-down");
	    	
	    	var userId = $(this).data("user");
			var content = $(this).data("content");
			var action = $(this).data("action");
			
	    	if (notYetSent) {
	    		$.ajax({
	        		type: "POST",
	    			url: "/recommendations",
	    			data: "userId="+userId+"&contentUuid="+content+"&action="+action
	        	});
	    		
	    		$(".fa", this).addClass("fa-thumbs-down").removeClass("fa-thumbs-o-down");
	    		$(".interested-button > .fa", $(this).parent()).removeClass("fa-thumbs-up").addClass("fa-thumbs-o-up");
	    	} else if (alreadySent) {
	    		action = "unmark-" + action;
	    		$.ajax({
	        		type: "POST",
	    			url: "/recommendations",
	    			data: "userId="+userId+"&contentUuid="+content+"&action="+action
	        	});
	    		
	    		$(".fa", this).removeClass("fa-thumbs-down").addClass("fa-thumbs-o-down");
	    	}
	    }
	    
	    $(".disinterested-button").click(disinterestedContent);
	
	    var interestedContent = function () {
	    	var notYetSent = $(".fa", this).hasClass("fa-thumbs-o-up");
	    	var alreadySent = $(".fa", this).hasClass("fa-thumbs-up");
	    	
	    	var userId = $(this).data("user");
			var content = $(this).data("content");
			var action = $(this).data("action");
			
			console.log(content);
			
	    	if (notYetSent) {
	    		$.ajax({
	        		type: "POST",
	    			url: "/recommendations",
	    			data: "userId="+userId+"&contentUuid="+content+"&action="+action
	        	});
	    		$(".fa", this).addClass("fa-thumbs-up").removeClass("fa-thumbs-o-up");
	    		$(".disinterested-button > .fa", $(this).parent()).removeClass("fa-thumbs-down").addClass("fa-thumbs-o-down");
	    	} else if (alreadySent) {
	    		action = "unmark-" + action;
	    		
	    		$.ajax({
	        		type: "POST",
	    			url: "/recommendations",
	    			data: "userId="+userId+"&contentUuid="+content+"&action="+action
	        	});
	    		
	    		$(".fa", this).removeClass("fa-thumbs-up").addClass("fa-thumbs-o-up");
	    	}
	    }
	    
	    $(".interested-button").click(interestedContent);
	    
	    var showAll = function (){
	    	var userId = $(this).data("user");
			var action = $(this).data("action");
			
	    	$.ajax({
				type: "POST",
				url: "/recommendations",
				data: "userId="+userId+"&action="+action
			});
	    	
	    	$(this).addClass('disabled');
	    	$(this).text('All Recommendations Unhidden');
	    };
	    
	    $(".show-all-recommendations").click(showAll);
	    
	    $(".search-result-page").each(function(){
	    	var parent = $(this).parent(".all-search-results");
	    	var id = "#" + $(this).attr("id");
	    	var tabId = id + "-tab";
	    	$(tabId, parent).click(function(){
	    		$(".search-result-page", parent).each(function(){
	    			$(this).addClass("hidden");
	    		});
	    		$(id, parent).removeClass("hidden");
	    		$(".result-page-tab", parent).each(function(){
	    			$(this).removeClass("btn-success").addClass("btn-default")
	    		});
	    		$(this).removeClass("btn-default").addClass("btn-success");
	    	});
	    });
	}
});$(function() {
    "use strict";

    if ($("#page-js").text().indexOf("searchByTag") > -1) { 
    
	    $("#tags-input").tagsInput({
	    	'width': '100%',
	    	'height': 'auto',
	    	'defaultText': 'Type Tags Here',
	    	'removeWithBackspace': true,
	    });
	    
	    var toggleBookmarkButton = function(){
	    	var userId = $(this).data("user");
			var content = $(this).data("content");
			var action = $(this).data("action");
			
	    	$.ajax({
				type: "POST",
				url: "/bookmark",
				data: "userId="+userId+"&contentUuid="+content+"&action="+action
			});
	    	
	    	if (action === "add") {
	    		$(this).data("action","remove");
	    		$('i', this).removeClass("fa-bookmark-o").addClass("fa-bookmark");
	    	} else {
	    		$(this).data("action","add");
	    		$('i', this).removeClass("fa-bookmark").addClass("fa-bookmark-o");
	    	}
	    };
		
		$(".toggle-bookmark-button").click(toggleBookmarkButton);
	    
	    $(".search-result-page").each(function(){
	    	var parent = $(this).parent(".box-body");
	    	var id = "#" + $(this).attr("id");
	    	var tabId = id + "-tab";
	    	$(tabId, parent).click(function(){
	    		$(".search-result-page", parent).each(function(){
	    			$(this).addClass("hidden");
	    		});
	    		$(id, parent).removeClass("hidden");
	    		$(".result-page-tab", parent).each(function(){
	    			$(this).removeClass("selected")
	    		});
	    		$(this).addClass("selected");
	    	});
	    });
	    
	    $(".add-recommended-tag").click(function(){
	    	var tag = $(this).data("tag");
	    	var input = $("#tags-input");
	    
	    	var newTag = "<span class=\"tag\"><span>"+ tag +"&nbsp;&nbsp;</span><a href=\"#\" title=\"Removing tag\"></a></span>"
	    	
	    	$(input).val($(input).val() + "," + tag);
	    	$(newTag).insertBefore("#tags-input_tag");
	
	    	$(this).remove();
	    });
    }
});$(function() {
    "use strict";
    
    if ($("#page-js").text().indexOf("tutorial") > -1) { 
    	$(".large-input-group-button").click(function(){
    		var value = $("#tags-input").val();
    		var url = "/search/" + value;
    		window.open(url, '_blank');
    	})
    	
    	var allBadges = $(".achievement-badge");
    	
    	$(allBadges).click(function(){
    		$(this).toggleClass("completed");
    	});
    	
    	$(".fa-bookmark-o.toggle-me").click(function(){
    		$(this).toggleClass("fa-bookmark-o").toggleClass("fa-bookmark");
    	});
    }
});