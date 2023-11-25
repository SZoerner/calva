(ns calva.repl.webview.core
  (:require
   [calva.util :as util]))

(def repl-output-webview-panel (atom nil))

(defn dispose-repl-output-webview-panel []
  (println "Disposing repl-output-webview-panel")
  (reset! repl-output-webview-panel nil))

(defn create-or-get-repl-output-webview-panel []
  (or @repl-output-webview-panel
      (let [webview-panel (.. @util/vscode -window (createWebviewPanel "calva:repl-output"
                                                                       "REPL Output"
                                                                       (.. @util/vscode -ViewColumn -Two)
                                                                       #js {:enableScripts true}))]
        (.. webview-panel (onDidDispose dispose-repl-output-webview-panel))
        (reset! repl-output-webview-panel webview-panel))))

(defn get-webview-html
  [js-src]
  (str "
<!DOCTYPE html>
<html lang=\"en\">
  <head>
    <meta charset=\"UTF-8\" />
    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />
    <title>REPL Output</title>
    <link
      rel=\"stylesheet\"
      href=\"https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/styles/atom-one-dark.min.css\"
    />
    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/highlight.min.js\"></script>
    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/languages/clojure.min.js\"></script>
    <script>
      hljs.highlightAll();
    </script>
  </head>
  <body>
    <!-- TODO: Disable inline scripts - see security section in webview docs -->
    <div id=\"output\"></div>

    <script>
      window.addEventListener('message', (event) => {
        const message = event.data; // The JSON data our extension sent
        const outputDiv = document.getElementById('output');

        switch (message.command) {
          case 'print-result':
            const resultParagraph = document.createElement('p');
            const text = document.createTextNode(message.result);
            resultParagraph.appendChild(text);
            outputDiv.appendChild(resultParagraph);
            break;
          case 'clear-output':
            outputDiv.innerHTML = '';
            break;
        }
      });
    </script>
    <script src=\"" js-src "\"></script>
  </body>
</html>"))

(defn post-message-to-webview [message]
  (.. @repl-output-webview-panel
      -webview
      (postMessage (clj->js message))))

(defn show-repl-output-webview-panel []
  (let [repl-output-webview-panel (create-or-get-repl-output-webview-panel)
        js-path (.. @util/vscode
                    -Uri
                    (joinPath (.. @util/context -extensionUri) "repl-output-ui" "js" "main.js"))
        js-src (.. repl-output-webview-panel -webview (asWebviewUri js-path))]
    (set! (.. repl-output-webview-panel -webview -html) (get-webview-html js-src))))

;; TODO: See if can send repl output to webview when it's hidden and see it once unhidden
;; "You cannot send messages to a hidden webview, even when retainContextWhenHidden is enabled."
;; https://code.visualstudio.com/api/extension-guides/webview#theming-webview-content

(comment
  (show-repl-output-webview-panel)

  (post-message-to-webview {:command "print-result"
                            :result "Hello world!!!"})

  (post-message-to-webview {:command "clear-output"})

  @repl-output-webview-panel

  ;; TODO: Don't worry about scrolling yet. We know we can do that. Explore other important unknowns first.

  :rcf)
