tinymce.init({
		    selector: "#htmltextarea",
		    
		    extended_valid_elements : "field",
		    custom_elements: "field",
		    
		    force_br_newlines : false,
		    force_p_newlines : false,
		    forced_root_block : '',
		    width: 600,
		    height: 300,
		    extended_valid_elements : ["a[class|name|href|target|title|onclick|rel],style,script[type|src],iframe[src|style|width|height|scrolling|marginwidth|marginheight|frameborder],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],$elements"],
		    
		    valid_children : "+body[style]",
		    
		    cleanup: false,
		    verify_html : false,
		    
		    theme: "modern",
		    width: 800,
		    height: 500,
		    plugins: [
		         "advlist autolink link image lists charmap print preview hr anchor pagebreak spellchecker",
		         "searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking",
		         "save table contextmenu directionality emoticons template paste textcolor"
		   ],
		   toolbar: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image | print preview media fullpage | forecolor backcolor emoticons", 
		   style_formats: [
		        {title: 'Bold text', inline: 'b'},
		        {title: 'Red text', inline: 'span', styles: {color: '#ff0000'}},
		        {title: 'Red header', block: 'h1', styles: {color: '#ff0000'}},
		        {title: 'Example 1', inline: 'span', classes: 'example1'},
		        {title: 'Example 2', inline: 'span', classes: 'example2'},
		        {title: 'Table styles'},
		        {title: 'Table row 1', selector: 'tr', classes: 'tablerow1'}
		    ],
		    
		    init_instance_callback : function(editor) {
		        // jw: this code is heavily borrowed from tinymce.jquery.js:12231 but modified so that it will
		        //     just remove the escaping and not add it back.
		        editor.serializer.addNodeFilter('script,style', function(nodes, name) {
		            var i = nodes.length, node, value, type;

		            function trim(value) {
		                /*jshint maxlen:255 */
		                /*eslint max-len:0 */
		                return value.replace(/(<!--\[CDATA\[|\]\]-->)/g, '\n')
		                        .replace(/^[\r\n]*|[\r\n]*$/g, '')
		                        .replace(/^\s*((<!--)?(\s*\/\/)?\s*<!\[CDATA\[|(<!--\s*)?\/\*\s*<!\[CDATA\[\s*\*\/|(\/\/)?\s*<!--|\/\*\s*<!--\s*\*\/)\s*[\r\n]*/gi, '')
		                        .replace(/\s*(\/\*\s*\]\]>\s*\*\/(-->)?|\s*\/\/\s*\]\]>(-->)?|\/\/\s*(-->)?|\]\]>|\/\*\s*-->\s*\*\/|\s*-->\s*)\s*$/g, '')
		                		.replace(/&#034;/g, '');
		            }
		            while (i--) {
		                node = nodes[i];
		                value = node.firstChild ? node.firstChild.value : '';

		                if (value.length > 0) {
		                    node.firstChild.value = trim(value);
		                }
		            }
		        });
		    }
		}); 