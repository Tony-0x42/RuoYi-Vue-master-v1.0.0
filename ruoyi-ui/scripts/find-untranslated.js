const fs = require('fs')
const path = require('path')
const glob = require('glob')

const ROOT = path.join(__dirname, '..')
const files = glob.sync('src/**/*.{vue,js}', { cwd: ROOT })

let found = 0

files.forEach(file => {
  const fullPath = path.join(ROOT, file)
  const content = fs.readFileSync(fullPath, 'utf-8')
  const lines = content.split(/\r?\n/)

  lines.forEach((line, idx) => {
    // Match common Chinese characters but skip lines already wrapped in $t(...)
    if (/[\u4e00-\u9fa5]/.test(line) && !line.includes('$t(')) {
      console.log(`${file}:${idx + 1}: ${line.trim()}`)
      found++
    }
  })
})

if (found === 0) {
  console.log('No untranslated Chinese text found.')
} else {
  console.log(`\nTotal: ${found} line(s) with untranslated Chinese text.`)
}
