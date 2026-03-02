<template>
  <div class="app-layout" :class="isDarkMode ? 'dark-mode' : ''">
    <div class="sidebar">
      <div class="logo-section">
        <img src="@/assets/hospital.jpg" alt="灵码挂号助手" class="logo-img"/>
        <span class="logo-text">智能挂号助手</span>
      </div>
      <el-button class="new-chat-button" @click="newChat">
        <i class="fa-solid fa-plus"></i>
        &nbsp;新会话
      </el-button>

      <div class="chat-history">
        <div class="history-title">历史会话</div>
        <div class="history-item active">当前会话</div>
      </div>
    </div>

    <div class="main-content">
      <div class="chat-header">
        <div class="header-title">智能挂号助手</div>
        <div class="header-actions">
          <i class="fa-solid fa-history history-icon"></i>
          <i
              :class="isDarkMode ? 'fa-solid fa-sun theme-icon' : 'fa-solid fa-moon theme-icon'"
              @click="toggleTheme"
              :title="isDarkMode ? '切换至亮色模式' : '切换至暗色模式'"
          ></i>
        </div>
      </div>

      <div class="chat-container">
        <div class="message-list" ref="messaggListRef">
          <!-- 欢迎消息与快捷功能按钮 -->
          <div class="welcome-message" v-if="messages.length === 0">
            <div class="welcome-icon">
              <i class="fa-solid fa-robot"></i>
            </div>
            <div class="welcome-text">
              <!-- 使用动态文本展示 -->
              <span v-if="showWelcomeText">{{ displayWelcomeText }}</span>
              <span v-else class="typing-indicator">
              <span class="dot"></span>
              <span class="dot"></span>
              <span class="dot"></span>
              </span>
            </div>

            <!-- 核心优化：三个功能按钮 -->
            <div class="welcome-actions">
              <el-button
                  class="action-btn department-btn"
                  @click="quickSend('路径规划')"
              >
                <i class="fa-solid fa-search"></i>&nbsp
                路径规划
              </el-button>
              <el-button
                  class="action-btn register-btn"
                  @click="quickSend('我要挂号')"
              >
                <i class="fa-solid fa-calendar-check"></i>&nbsp
                我要挂号
              </el-button>
              <el-button
                  class="action-btn cancel-btn"
                  @click="quickSend('取消预约')"
              >
                <i class="fa-solid fa-calendar-times"></i>&nbsp
                取消预约
              </el-button>
            </div>
            <!-- 额外快捷问题 -->
            <div class="welcome-suggestions">
              <el-button size="small" class="suggestion-btn" @click="quickSend('医疗报销有哪些通用材料')">医疗报销所需材料</el-button>
              <el-button size="small" class="suggestion-btn" @click="quickSend('合疗报销有哪些通用材料')">合疗报销所需材料</el-button>
            </div>
          </div>

          <div
              v-for="(message, index) in messages"
              :key="message.id"
              :class="
              message.isUser ? 'message user-message' : 'message bot-message'
            "
          >
            <i
                :class="
                message.isUser
                  ? 'fa-solid fa-user message-icon'
                  : 'fa-solid fa-robot message-icon'
              "
            ></i>
            <div class="message-content">
              <span v-html="message.content"></span>
              <span
                  class="loading-dots"
                  v-if="message.isThinking || message.isTyping"
              >
                <span class="dot"></span>
                <span class="dot"></span>
                <span class="dot"></span>
              </span>
            </div>
          </div>
        </div>

        <div class="input-container">
          <el-input
              v-model="inputMessage"
              placeholder="请输入您想咨询的问题..."
              @keyup.enter="sendMessage"
              class="chat-input"
              clearable
          ></el-input>

          <el-button
              @click="sendMessage"
              :disabled="isSending || !inputMessage.trim()"
              class="send-button"
          >
            <i class="fa-solid fa-paper-plane"></i>
          </el-button>
        </div>
      </div>

      <div class="chat-footer">
        <span>温馨提示：本助手提供的信息仅供参考，具体以医院实际规定为准</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import {nextTick, onMounted, ref, watch} from 'vue'
import axios from 'axios'
import {v4 as uuidv4} from 'uuid'

const messaggListRef = ref()
const isSending = ref(false)
const uuid = ref()
const inputMessage = ref('')
const messages = ref([])
// 新增主题切换相关变量
const isDarkMode = ref(false)
// 欢迎消息相关状态
const welcomeText = ref('您好！我是智能挂号助手，有什么可以帮助您的吗？')
const displayWelcomeText = ref('')
const showWelcomeText = ref(false)
const showWelcomeActions = ref(false)
const showWelcomeSuggestions = ref(false)
let typingTimer = null

// 快捷发送功能：填入指定内容并发送
const quickSend = (text) => {
  inputMessage.value = text
  sendMessage()
}

// 新增主题切换方法
const toggleTheme = () => {
  isDarkMode.value = !isDarkMode.value
  // 保存主题偏好到本地存储
  localStorage.setItem('darkMode', isDarkMode.value ? '1' : '0')
}

onMounted(() => {
  initUUID()
  // 初始化主题：优先使用本地存储，其次使用系统偏好
  const savedMode = localStorage.getItem('darkMode')
  if (savedMode !== null) {
    isDarkMode.value = savedMode === '1'
  } else {
    // 检测系统主题偏好
    isDarkMode.value = window.matchMedia('(prefers-color-scheme: dark)').matches
  }
  if(messages.value.length === 0){
    startTypingEffect()
  }
  watch(messages, () => scrollToBottom(), {deep: true})
})
onMounted(() =>{
  if (typingTimer){
    clearInterval(typingTimer)
  }
})
const scrollToBottom = () => {
  if (messaggListRef.value) {
    setTimeout(() => {
      messaggListRef.value.scrollTop = messaggListRef.value.scrollHeight
    }, 100)
  }
}

const sendMessage = () => {
  if (inputMessage.value.trim()) {
    sendRequest(inputMessage.value.trim())
    inputMessage.value = ''
  }
}

const sendRequest = (message) => {
  isSending.value = true
  const userMsg = {
    id: uuidv4(),
    isUser: true,
    content: message,
    isTyping: false,
    isThinking: false,
  }

  messages.value.push(userMsg)

  const botMsg = {
    id: uuidv4(),
    isUser: false,
    content: '',
    isTyping: true,
    isThinking: false,
  }
  messages.value.push(botMsg)
  const lastMsg = messages.value[messages.value.length - 1]
  scrollToBottom()

  axios
      .post(
          '/api/agent/chat',
          {memoryId: uuid.value, message: message},
          {
            responseType: 'stream',
            onDownloadProgress: (e) => {
              const fullText = e.event.target.responseText
              let newText = fullText.substring(lastMsg.content.length)
              lastMsg.content += newText
              scrollToBottom()
            },
          }
      )
      .then(() => {
        messages.value[messages.value.length - 1].isTyping = false
        isSending.value = false
      })
      .catch((error) => {
        console.error('流式错误:', error)
        messages.value[messages.value.length - 1].content = '请求失败，请重试'
        messages.value[messages.value.length - 1].isTyping = false
        isSending.value = false
      })
}

const initUUID = () => {
  let storedUUID = localStorage.getItem('user_uuid')
  if (!storedUUID) {
    storedUUID = uuidToNumber(uuidv4())
    localStorage.setItem('user_uuid', storedUUID)
  }
  uuid.value = storedUUID
}

const uuidToNumber = (uuid) => {
  let number = 0
  for (let i = 0; i < uuid.length && i < 6; i++) {
    const hexValue = uuid[i]
    number = number * 16 + (parseInt(hexValue, 16) || 0)
  }
  return number % 1000000
}

const newChat = () => {
  localStorage.removeItem('user_uuid')
  uuid.value = null
  initUUID()
  messages.value = []
  inputMessage.value = ''
  displayWelcomeText.value = ''
  showWelcomeText.value = false
  showWelcomeActions.value = false
  showWelcomeSuggestions.value = false

  nextTick(() => {
    startTypingEffect()
  })
}
const startTypingEffect = () => {
  let index = 0
      const text = welcomeText.value

      // 清除之前的定时器
          if (typingTimer) {
      clearInterval(typingTimer)
    }

      // 显示打字指示器
          showWelcomeText.value = false

      // 延迟开始打字效果
          setTimeout(() => {
              showWelcomeText.value = true

              typingTimer = setInterval(() => {
                      if (index < text.length) {
                          displayWelcomeText.value += text.charAt(index)
                              index++
                            } else {
                          clearInterval(typingTimer)

                          // 打字完成后显示按钮和建议
                              setTimeout(() => {
                                  showWelcomeActions.value = true

                                      setTimeout(() => {
                                          showWelcomeSuggestions.value = true
                                            }, 500)
                                    }, 300)
                        }
                    }, 50) // 每个字符的间隔时间
                }, 500) // 开始打字前的延迟
    }
</script>

<style scoped>
.app-layout {
  display: flex;
  height: 100vh;
  background-color: #F3F4F6;
  overflow: hidden;
  transition: background-color 0.3s ease;
}

/* 全局滚动条样式 - 豆包风格：细窄、靠右、淡色 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: transparent;
}

::-webkit-scrollbar-thumb {
  background-color: rgba(156, 163, 175, 0.5);
  border-radius: 3px;
  transition: background-color 0.2s ease;
}

::-webkit-scrollbar-thumb:hover {
  background-color: rgba(156, 163, 175, 0.8);
}

/* 侧边栏样式 */
.sidebar {
  width: 260px;
  background-color: #ffffff;
  border-right: 1px solid #ebebeb;
  display: flex;
  flex-direction: column;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.03);
  z-index: 10;
  overflow-y: auto;
  transition: all 0.3s ease;
}

/* 侧边栏滚动条调整 */
.sidebar::-webkit-scrollbar {
  width: 4px;
}

.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24px 0 16px;
  border-bottom: 1px solid #f0f0f0;
  transition: border-color 0.3s ease;
}

.logo-img {
  width: 100px;
  height: 100px;
  border-radius: 12px;
  object-fit: cover;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-top: 8px;
  transition: color 0.3s ease;
}

.new-chat-button {
  width: calc(100% - 32px);
  margin: 16px 16px 0;
  height: 40px;
  border-radius: 8px;
  background-color: #e8f3ff;
  color: #1677ff;
  border: 1px solid #d1e3ff;
  transition: all 0.2s ease;
}

.new-chat-button:hover {
  background-color: #d1e3ff;
}

/* 会话历史样式 */
.chat-history {
  margin-top: 24px;
  flex: 1;
}

.history-title {
  font-size: 12px;
  color: #8c8c8c;
  padding: 0 20px 8px;
  font-weight: 500;
  transition: color 0.3s ease;
}

.history-item {
  padding: 12px 20px;
  font-size: 14px;
  color: #333;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
}

.history-item::before {
  content: "";
  width: 4px;
  height: 4px;
  border-radius: 50%;
  background-color: #ddd;
  margin-right: 8px;
  transition: background-color 0.3s ease;
}

.history-item.active {
  background-color: #e8f3ff;
  color: #1677ff;
  font-weight: 500;
}

.history-item.active::before {
  background-color: #1677ff;
}

.history-item:hover:not(.active) {
  background-color: #f5f5f5;
}

/* 主内容区样式 */
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
  padding-right: 8px;
  transition: background-color 0.3s ease;
}

/* 顶部导航 */
.chat-header {
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  background-color: #fff;
  border-bottom: 1px solid #f0f0f0;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03);
  position: sticky;
  top: 0;
  z-index: 5;
  transition: all 0.3s ease;
}

.header-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  transition: color 0.3s ease;
}

.header-actions {
  display: flex;
  gap: 16px;
}

.header-actions i {
  color: #666;
  font-size: 18px;
  cursor: pointer;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.2s ease;
}

.header-actions i:hover {
  background-color: #f0f0f0;
  color: #333;
}

/* 聊天容器 */
.chat-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 0 24px;
  max-width: 800px;
  margin: 0 auto;
  width: 100%;
}

/* 欢迎消息样式 */
.welcome-message {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  text-align: center;
  color: #666;
  margin-top: 40px;
  transition: color 0.3s ease;
}

.welcome-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background-color: #f0f7ff;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
  transition: background-color 0.3s ease;
}

.welcome-icon i {
  font-size: 36px;
  color: #1677ff;
  transition: color 0.3s ease;
}

.welcome-text {
  font-size: 18px;
  color: #333;
  margin-bottom: 24px;
  transition: color 0.3s ease;
}

/* 欢迎区功能按钮样式 */
.welcome-actions {
  display: flex;
  gap: 16px;
  margin-bottom: 32px;
  width: 100%;
  justify-content: center;
  flex-wrap: wrap;
}

.action-btn {
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 140px;
  transition: all 0.3s ease;
  border: none;
}

.department-btn {
  background-color: #e6f4ff;
  color: #1890ff;
}

.department-btn:hover {
  background-color: #bae0ff;
  color: #096dd9;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.15);
}

.register-btn {
  background-color: #f6ffed;
  color: #52c41a;
}

.register-btn:hover {
  background-color: #d9f7be;
  color: #389e0d;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(82, 196, 26, 0.15);
}

.cancel-btn {
  background-color: #fff2f0;
  color: #f5222d;
}

.cancel-btn:hover {
  background-color: #ffccc7;
  color: #cf1322;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(245, 34, 45, 0.15);
}

/* 快捷问题样式 */
.welcome-suggestions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  justify-content: center;
  width: 100%;
}

.suggestion-btn {
  background-color: #fff;
  border: 1px solid #e5e7eb;
  color: #333;
  border-radius: 16px;
  padding: 6px 16px;
  font-size: 14px;
  transition: all 0.2s ease;
}

.suggestion-btn:hover {
  background-color: #f9fafb;
  border-color: #d1d5db;
  color: #1677ff;
}

/* 消息列表样式 */
.message-list {
  flex: 1;
  padding: 20px 0;
  display: flex;
  flex-direction: column;
}

/* 消息样式 */
.message {
  margin-bottom: 20px;
  display: flex;
  max-width: 100%;
  animation: fadeIn 0.2s ease forwards;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.message-icon {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  margin-right: 12px;
  background-color: #f0f2f5;
  color: #666;
  transition: all 0.3s ease;
}

.user-message .message-icon {
  margin-left: 12px;
  margin-right: 0;
  background-color: #1677ff;
  color: white;
}

.message-content {
  max-width: calc(100% - 80px);
  line-height: 1.6;
  padding: 12px 16px;
  border-radius: 10px;
  font-size: 15px;
  word-break: break-word;
  transition: all 0.3s ease;
}

.bot-message .message-content {
  background-color: #fff;
  border: 1px solid #f0f0f0;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  color: #333;
  border-top-left-radius: 4px;
}

.user-message .message-content {
  background-color: #1677ff;
  color: white;
  border-top-right-radius: 4px;
}

.user-message {
  flex-direction: row-reverse;
  align-self: flex-end;
}

/* 输入区域样式 */
.input-container {
  display: flex;
  align-items: center;
  padding: 12px;
  background-color: #fff;
  border: 1px solid #f0f0f0;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
  position: sticky;
  bottom: 0;
  z-index: 5;
  background-color: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(4px);
  transition: all 0.3s ease;
}

.chat-input {
  flex: 1;
  border: none !important;
  font-size: 15px;
  padding: 0 8px;
  height: 40px;
}

.chat-input .el-input__wrapper {
  border: none !important;
  box-shadow: none !important;
  padding: 0 !important;
  height: 40px;
  background-color: transparent !important;
}

.send-button {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  padding: 0;
  background-color: #1677ff;
  color: white;
  margin-left: 8px;
  transition: all 0.2s ease;
}

.send-button:disabled {
  background-color: #a0cfff;
  cursor: not-allowed;
}

.send-button:not(:disabled):hover {
  background-color: #0958d9;
  transform: scale(1.05);
}

/* 加载动画 */
.loading-dots {
  display: inline-flex;
  align-items: center;
  margin-left: 4px;
}

.dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  background-color: currentColor;
  border-radius: 50%;
  margin: 0 2px;
  animation: bounce 1.4s infinite ease-in-out both;
}

.bot-message .dot {
  background-color: #888;
}

.user-message .dot {
  background-color: rgba(255, 255, 255, 0.7);
}

.dot:nth-child(1) {
  animation-delay: -0.32s;
}

.dot:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes bounce {
  0%, 80%, 100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1);
  }
}

/* 底部信息 */
.chat-footer {
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  color: #999;
  border-top: 1px solid #f0f0f0;
  margin-top: auto;
  transition: all 0.3s ease;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sidebar {
    width: 100%;
    height: auto;
    position: absolute;
    z-index: 100;
    max-height: 100vh;
    transform: translateX(-100%);
    transition: transform 0.3s ease;
  }

  .sidebar.show {
    transform: translateX(0);
  }

  .main-content {
    width: 100%;
    padding-right: 4px;
  }

  .chat-container {
    padding: 0 12px;
  }

  .message-content {
    max-width: calc(100% - 60px);
    font-size: 14px;
    padding: 10px 14px;
  }

  .chat-header {
    padding: 0 12px;
  }

  .header-actions {
    gap: 8px;
  }

  .welcome-message {
    padding: 20px 10px;
  }

  .welcome-actions {
    gap: 12px;
  }

  .action-btn {
    padding: 10px 16px;
    font-size: 14px;
    min-width: auto;
    flex: 1;
    justify-content: center;
  }
}

/* 暗色模式样式 */
.app-layout.dark-mode {
  background-color: #1a1a1a;
}

.app-layout.dark-mode .sidebar {
  background-color: #2c2c2c;
  border-right: 1px solid #3d3d3d;
}

.app-layout.dark-mode .logo-text,
.app-layout.dark-mode .history-item,
.app-layout.dark-mode .header-title,
.app-layout.dark-mode .welcome-text {
  color: #e0e0e0;
}

.app-layout.dark-mode .history-title,
.app-layout.dark-mode .welcome-message,
.app-layout.dark-mode .chat-footer {
  color: #9e9e9e;
}

.app-layout.dark-mode .history-item.active {
  background-color: #1e40af;
  color: #fff;
}

.app-layout.dark-mode .new-chat-button {
  background-color: #1e3a8a;
  color: #bfdbfe;
  border-color: #3b82f6;
}

.app-layout.dark-mode .new-chat-button:hover {
  background-color: #1e40af;
}

.app-layout.dark-mode .chat-header {
  background-color: #2c2c2c;
  border-bottom: 1px solid #3d3d3d;
}

.app-layout.dark-mode .header-actions i {
  color: #bbb;
}

.app-layout.dark-mode .header-actions i:hover {
  background-color: #3d3d3d;
  color: #fff;
}

.app-layout.dark-mode .welcome-icon {
  background-color: #1e3a8a;
}

.app-layout.dark-mode .welcome-icon i {
  color: #69b1ff;
}

.app-layout.dark-mode .suggestion-btn {
  background-color: #3d3d3d;
  border-color: #4d4d4d;
  color: #e0e0e0;
}

.app-layout.dark-mode .suggestion-btn:hover {
  background-color: #4d4d4d;
  border-color: #5d5d5d;
}

.app-layout.dark-mode .bot-message .message-content {
  background-color: #3d3d3d;
  border-color: #4d4d4d;
  color: #e0e0e0;
}

.app-layout.dark-mode .input-container {
  background-color: rgba(44, 44, 44, 0.95);
  border-color: #3d3d3d;
}

.app-layout.dark-mode .tool-icon {
  color: #bbb;
}

.app-layout.dark-mode .tool-icon:hover {
  background-color: #3d3d3d;
  color: #fff;
}

.app-layout.dark-mode .chat-footer {
  background-color: #2c2c2c;
  border-top: 1px solid #3d3d3d;
}

.app-layout.dark-mode .department-btn {
  background-color: #1e3a8a;
  color: #bfdbfe;
}

.app-layout.dark-mode .department-btn:hover {
  background-color: #1e40af;
  color: #e6f4ff;
}

.app-layout.dark-mode .register-btn {
  background-color: #274910;
  color: #d9f7be;
}

.app-layout.dark-mode .register-btn:hover {
  background-color: #365f12;
  color: #f6ffed;
}

.app-layout.dark-mode .cancel-btn {
  background-color: #5c151f;
  color: #ffccc7;
}

.app-layout.dark-mode .cancel-btn:hover {
  background-color: #721c24;
  color: #fff2f0;
}

.app-layout.dark-mode .message-icon {
  background-color: #3d3d3d;
  color: #bbb;
}

.app-layout.dark-mode ::-webkit-scrollbar-thumb {
  background-color: rgba(107, 114, 128, 0.5);
}

.app-layout.dark-mode ::-webkit-scrollbar-thumb:hover {
  background-color: rgba(107, 114, 128, 0.8);
}

.app-layout.dark-mode .history-item::before {
  background-color: #555;
}

.app-layout.dark-mode .history-item:hover:not(.active) {
  background-color: #3d3d3d;
}
.theme-icon {
  position: relative;
}
 /* 亮色模式太阳图标 */
.fa-sun {
  color: #ff9800;
}
 /* 暗色模式月亮图标 */
.fa-moon {
  color: #909399; }
</style>