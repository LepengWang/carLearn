# 智驾通 - Android 客户端架构指南

## 1. 技术栈规范
- **语言**: Kotlin (1.9+)
- **架构模式**: MVVM + Clean Architecture
- **UI 框架**: 原生 XML + Material Design 3 (部分复杂交互使用 Jetpack Compose)
- **异步处理**: Kotlin Coroutines + Flow
- **网络层**: Retrofit2 + OkHttp3 (支持 SSE 流式传输)
- [cite_start]**数据库**: Room (用于本地题库缓存与历史记录) 
- [cite_start]**图像处理**: CameraX + OpenCV/MediaPipe 

## 2. 核心模块划分
- `:app`: 壳工程，包含 DI (Hilt/Koin) 配置。
- [cite_start]`:feature:exam`: 刷题系统、模拟考试、灯光模拟逻辑 。
- [cite_start]`:feature:ai_assistant`: AI 讲题悬浮窗、RAG 问答界面、语音交互状态机 。
- [cite_start]`:feature:vision`: 交通标志实时检测与 OCR 模块 。
- [cite_start]`:core:ui`: 封装通用自定义 View（如雷达图、知识图谱看板）。

## 3. 开发原则
- 所有的 AI 回复必须遵循流式展示（Streaming）效果。
- 摄像头预览需在主线程外处理，确保 UI 帧率不低于 60fps。