// Variables globales
      // NOTA: Cambiamos el nombre de la clave guardada para reflejar que usamos Groq
      let apiKey = localStorage.getItem("groq_api_key") || "";
      let isLoading = false;

      // Elementos del DOM
      const settingsPanel = document.getElementById("settingsPanel");
      const btnSettings = document.getElementById("btnSettings");
      const btnSaveKey = document.getElementById("btnSaveKey");
      const apiKeyInput = document.getElementById("apiKeyInput");
      const messageInput = document.getElementById("messageInput");
      const btnSend = document.getElementById("btnSend");
      const messagesContainer = document.getElementById("messagesContainer");
      const messagesWrapper = document.getElementById("messagesWrapper");
      const welcomeScreen = document.getElementById("welcomeScreen");
      
      // Elementos del Modal
      const openChatBtn = document.getElementById("openChatBtn");
      const chatModalOverlay = document.getElementById("chatModalOverlay");
      const chatModalContent = document.getElementById("chatModalContent");
      const btnCloseModal = document.getElementById("btnCloseModal");

      // Funciones de control del Modal
      function openChatModal() {
        chatModalOverlay.classList.add("open");
        chatModalContent.style.display = "flex";
        scrollToBottom(); 

        // Si la clave no está, abre la configuración
        if (!apiKey) {
            settingsPanel.classList.add("show");
        }
      }

      function closeChatModal() {
        chatModalOverlay.classList.remove("open");
      }

      // Cargar API key al inicio
      if (apiKey) {
        apiKeyInput.value = apiKey;
      }

      // Event Listeners del Modal
      openChatBtn.addEventListener("click", openChatModal);
      btnCloseModal.addEventListener("click", closeChatModal);
      
      // Cerrar modal al hacer clic fuera
      chatModalOverlay.addEventListener("click", (e) => {
          if (e.target === chatModalOverlay) {
              closeChatModal();
          }
      });
      
      // Toggle settings
      btnSettings.addEventListener("click", () => {
        settingsPanel.classList.toggle("show");
      });

      // Guardar API key
      btnSaveKey.addEventListener("click", () => {
        const key = apiKeyInput.value.trim();
        if (key) {
          apiKey = key;
          // Guardamos la clave con el nuevo nombre
          localStorage.setItem("groq_api_key", key); 
          settingsPanel.classList.remove("show");
          alert("API Key guardada correctamente");
        } else {
          alert("Por favor ingresa una API key válida");
        }
      });

      // Auto-resize textarea
      //messageInput.addEventListener("input", function () {
       // this.style.height = "auto";
       // this.style.height = Math.min(this.scrollHeight, 150) + "px";
     // });

      // Enviar mensaje
      btnSend.addEventListener("click", sendMessage);
      messageInput.addEventListener("keypress", (e) => {
        if (e.key === "Enter" && !e.shiftKey) {
          e.preventDefault();
          sendMessage();
        }
      });

      // ********************************************
      // 🟢 FUNCIÓN PRINCIPAL DE GROQ (Actualizada)
      // ********************************************
      async function sendMessage() {
        const message = messageInput.value.trim();

        if (!message || isLoading) return;

        if (!apiKey) {
          alert("Por favor configura tu API key primero");
          settingsPanel.classList.add("show");
          return;
        }

        // Ocultar pantalla de bienvenida
        if (welcomeScreen) {
          welcomeScreen.style.display = "none";
        }

        // Agregar mensaje del usuario
        addMessage("user", message);
        messageInput.value = "";
        messageInput.style.height = "auto";

        // Mostrar indicador de escritura
        showTypingIndicator();
        isLoading = true;
        btnSend.disabled = true;

        try {
          const response = await fetch(
            // Endpoint de Groq
            `https://api.groq.com/openai/v1/chat/completions`,
            {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
                // La clave se pasa en la cabecera 'Authorization' como un 'Bearer Token'
                Authorization: `Bearer ${apiKey}`,
              },
              body: JSON.stringify({

                model: "llama-3.3-70b-versatile", 
                messages: [
                  { 
                    role: "system", 
                    content: "Eres un Asistente Experto en Horticultura y Huerta Orgánica. Tu objetivo es responder todas las preguntas del usuario sobre cultivos, siembra, plagas, suelos y cosechas en el contexto de una huerta casera. Utiliza un tono amigable, didáctico y enfocado en métodos naturales y sostenibles. Responde siempre de forma práctica y concisa. NO hables de temas que no sean de jardinería o agricultura, da respuestas concretas y no tan extensas, ayuda al usuario con informacion de productos agricolas, lacteos y demas." 
                  },
                  // Mensaje del Usuario
                  { role: "user", content: message }
                ],
              }),
            }
          );
          const data = await response.json();

          hideTypingIndicator();

          if (data.error) {
            // Manejo de errores específico de la API de Groq/OpenAI
            throw new Error(data.error.message || "Error en la API de Groq");
          }

          // La respuesta se parsea usando el formato de OpenAI
          const aiResponse = data.choices[0].message.content;
          addMessage("assistant", aiResponse);
        } catch (error) {
          hideTypingIndicator();
          console.error("Error:", error);
          addMessage(
            "assistant",
            `❌ Error: ${error.message}. Por favor verifica tu API key y que el modelo 'llama3-8b-8192' esté disponible en Groq.`
          );
        } finally {
          isLoading = false;
          btnSend.disabled = false;
        }
      }
      // ********************************************
      // 🟢 FIN FUNCIÓN PRINCIPAL DE GROQ
      // ********************************************

      function addMessage(role, content) {
        const messageDiv = document.createElement("div");
        messageDiv.className = `message ${role}`;

        const avatar = document.createElement("div");
        avatar.className = "message-avatar";
        avatar.textContent = role === "user" ? "👤" : "🤖";

        const contentDiv = document.createElement("div");
        contentDiv.className = "message-content";
        contentDiv.textContent = content;

        messageDiv.appendChild(avatar);
        messageDiv.appendChild(contentDiv);
        messagesWrapper.appendChild(messageDiv);

        scrollToBottom();
      }

      function showTypingIndicator() {
        const typingDiv = document.createElement("div");
        typingDiv.className = "typing-indicator";
        typingDiv.id = "typingIndicator";

        typingDiv.innerHTML = `
                <div class="message-avatar" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white;">🤖</div>
                <div class="typing-dots">
                    <div class="dot"></div>
                    <div class="dot"></div>
                    <div class="dot"></div>
                </div>
            `;

        messagesWrapper.appendChild(typingDiv);
        scrollToBottom();
      }

      function hideTypingIndicator() {
        const indicator = document.getElementById("typingIndicator");
        if (indicator) {
          indicator.remove();
        }
      }

      function scrollToBottom() {
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
      }