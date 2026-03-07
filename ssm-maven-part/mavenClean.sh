#!/bin/zsh

  REPOSITORY_PATH=~/.m2/repository
  echo "正在搜索 lastUpdated 文件..."

  # 统计数量
  count=$(find "$REPOSITORY_PATH" -name "*lastUpdated*" -type f 2>/dev/null | wc -l | tr -d ' ')
  if [ "$count" -eq 0 ]; then
      echo "没有找到需要清理的文件"
  else
      # 统计大小
      size=$(find "$REPOSITORY_PATH" -name "*lastUpdated*" -type f -print0 2>/dev/null | xargs -0 du -ch 2>/dev/null |
  tail -1 | awk '{print $1}')

      echo "找到 $count 个文件，总大小: $size"
      echo "正在删除..."
      find "$REPOSITORY_PATH" -name "*lastUpdated*" -type f -delete 2>/dev/null
      echo "清理完成"
  fi