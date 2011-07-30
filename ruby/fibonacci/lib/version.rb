
class Fibonacci
	module VERSION
    MAJOR = 0
		MINOR = 0
		PATCH = 1
		BUILD = 'interview'
		
		#e.g., 0.1.0
		STRING = [MAJOR, MINOR, PATCH].compact.join('.')
		
		#e.g., 0.1.0-interview
		FILE_STRING = STRING + '-' + [BUILD].compact.join('-')
  end
end
