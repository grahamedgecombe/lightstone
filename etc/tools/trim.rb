#!/usr/bin/env ruby
Dir.glob('**/*.{java,rb,xml}').each do |name|
  lines = []
  file = File.new(name, 'r')
  begin
    while (line = file.gets) != nil
      lines << line.rstrip
    end
  ensure
    file.close
  end

  file = File.new(name, 'w')
  begin
    last_was_empty = false
    lines.each do |line|
      file.puts(line)
      last_was_empty = line.strip.length == 0
    end
    if not last_was_empty
      file.puts
    end
  ensure
    file.close
  end
end

