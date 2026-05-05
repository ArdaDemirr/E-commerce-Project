/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,ts}"],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        primary:   { DEFAULT: '#6366F1', dark: '#4F46E5' },
        secondary: { DEFAULT: '#8B5CF6', dark: '#7C3AED' },
        accent:    { DEFAULT: '#06B6D4', dark: '#0891B2' },
        surface:   { DEFAULT: '#1E293B', light: '#334155' },
        background:{ DEFAULT: '#0F172A' },
      },
      animation: {
        'fade-in':    'fadeIn 0.3s ease-in-out',
        'slide-up':   'slideUp 0.4s ease-out',
        'slide-in':   'slideIn 0.3s ease-out',
        'pulse-slow': 'pulse 3s cubic-bezier(0.4,0,0.6,1) infinite',
        'glow':       'glow 2s ease-in-out infinite alternate',
      },
      keyframes: {
        fadeIn:  { '0%': { opacity: '0' }, '100%': { opacity: '1' } },
        slideUp: { '0%': { transform: 'translateY(20px)', opacity: '0' }, '100%': { transform: 'translateY(0)', opacity: '1' } },
        slideIn: { '0%': { transform: 'translateX(-20px)', opacity: '0' }, '100%': { transform: 'translateX(0)', opacity: '1' } },
        glow:    { '0%': { boxShadow: '0 0 5px #6366F1' }, '100%': { boxShadow: '0 0 20px #6366F1, 0 0 40px #8B5CF6' } },
      },
      backdropBlur: { xs: '2px' },
    }
  },
  plugins: [require('@tailwindcss/forms'), require('@tailwindcss/typography')]
}

