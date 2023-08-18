module.exports = {
  // 템플릿 파일의 경로 설정
  content: ['./src/**/*.{js,jsx,ts,tsx}'],
  theme: {
    extend: {
      width: {
        100: '25rem',
        120: '30rem',
      },
      maxHeight: {
        half: '50%',
      },
      minWidth: {
        half: '50%',
      },
      keyframes: {
        wiggle: {
          '0%, 100%': { transform: 'rotate(-2deg)' },
          '50%': { transform: 'rotate(6deg)' },
        },
        slide: {
          '0%': {
            transform: 'translateX(-200%) rotate(+6deg)',
            animationTimingFunction: 'cubic-bezier(0, 0, 0.2, 1)',
          },
          '6%': {
            transform: 'translateX(-150%) translateY(-20%) rotate(-2deg)',
            animationTimingFunction: 'cubic-bezier(0.8, 0, 1, 1)',
          },
          '12%': {
            transform: 'translateX(-100%) rotate(+6deg)',
            animationTimingFunction: 'cubic-bezier(0, 0, 0.2, 1)',
          },
          '18%': {
            transform: 'translateX(-50%) translateY(-20%) rotate(-2deg)',
            animationTimingFunction: 'cubic-bezier(0.8, 0, 1, 1)',
          },
          '25%': {
            transform: 'translateX(0%) rotate(+6deg)',
            animationTimingFunction: 'cubic-bezier(0, 0, 0.2, 1)',
          },
          '31%': {
            transform: 'translateX(50%) translateY(-20%) rotate(-2deg)',
            animationTimingFunction: 'cubic-bezier(0.8, 0, 1, 1)',
          },
          '37%': {
            transform: 'translateX(100%) rotate(+6deg)',
            animationTimingFunction: 'cubic-bezier(0, 0, 0.2, 1)',
          },
          '43%': {
            transform: 'translateX(150%) translateY(-20%) rotate(-2deg)',
            animationTimingFunction: 'cubic-bezier(0.8, 0, 1, 1)',
          },
          '49.9%': {
            transform: 'translateX(200%) rotate(+6deg)',
            animationTimingFunction: 'cubic-bezier(0.8, 0, 1, 1)',
          },
          '50%': {
            transform: 'translateX(200%) scaleX(-1)',
            animationTimingFunction: 'cubic-bezier(0, 0, 0.2, 1)',
          },
          '56%': {
            transform:
              'translateX(150%) translateY(-20%) scaleX(-1) rotate(-2deg)',
            animationTimingFunction: 'cubic-bezier(0.8, 0, 1, 1)',
          },
          '62%': {
            transform: 'translateX(100%) scaleX(-1)',
            animationTimingFunction: 'cubic-bezier(0, 0, 0.2, 1)',
          },
          '68%': {
            transform:
              'translateX(50%) translateY(-20%) scaleX(-1) rotate(-2deg)',
            animationTimingFunction: 'cubic-bezier(0.8, 0, 1, 1)',
          },
          '75%': {
            transform: 'translateX(0%) scaleX(-1)',
            animationTimingFunction: 'cubic-bezier(0, 0, 0.2, 1)',
          },
          '81%': {
            transform:
              'translateX(-50%) translateY(-20%) scaleX(-1) rotate(-2deg)',
            animationTimingFunction: 'cubic-bezier(0.8, 0, 1, 1)',
          },
          '87%': {
            transform: 'translateX(-100%) scaleX(-1)',
            animationTimingFunction: 'cubic-bezier(0, 0, 0.2, 1)',
          },
          '93%': {
            transform:
              'translateX(-150%) translateY(-20%) scaleX(-1) rotate(-2deg)',
            animationTimingFunction: 'cubic-bezier(0.8, 0, 1, 1)',
          },
          '99.9%': {
            transform: 'translateX(-200%) scaleX(-1)',
            animationTimingFunction: 'cubic-bezier(0, 0, 0.2, 1)',
          },
          '100%': {
            transform: 'translateX(-200%) scaleX(1)',
            animationTimingFunction: 'cubic-bezier(0, 0, 0.2, 1)',
          },
        },
      },
      animation: {
        slide: 'slide 8s infinite ',
        wiggle: 'wiggle 1s ease-in-out infinite',
      },
    },
    colors: {
      white: '#ffffff',
      grey: '#D4D4D4',
      lightgrey: '#F6F6F6',
      darkgrey: '#00000080',
      black: '#18181B',
      primary_dark: '#36194D',
      primary: '#7532A8',
      primary_2: '#A77CC7',
      primary_3: '#D9C6E7',
      primary_4: '#F1EAF6',
      secondary: '#22C55E80',
      yellow: '#F9E000',
      kakao: '#FDE500',
      pink: '#FF9090',
      orange: '#FAAF1D',
      beige: '#D5BC9F',
      red: '#FF0000',
      shadowbg: '#36194D15',
    },
  },
  plugins: [],
};
