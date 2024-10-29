//회원가입 유효성 검사
//== 필수 ===========
document.getElementById('id').addEventListener('blur', validateId);
document.getElementById('pwd').addEventListener('blur', validatePwd);
document.getElementById('pwdCheck').addEventListener('blur', validatePwdCheck);
document.getElementById('name').addEventListener('blur', validateName);
document.getElementById('phone').addEventListener('blur', validatePhone);
//== 선택 ===========
document.getElementById('email').addEventListener('blur', validateEmail);
document.getElementById('birth').addEventListener('blur', validateBirth);

//회원가입 form 제출
document.getElementById('signupForm').addEventListener('submit', async function(e){
  e.preventDefault();

  const isValidId = await validateId();
  const isValidPwd = validatePwd();
  const isValidPwdCheck = validatePwdCheck();
  const isValidName = validateName();
  const isValidPhone = validatePhone();
  const isValidEmail = validateEmail();
  const isValidBirth = validateBirth();

  //모두 통과하면 제출
  if (isValidId && isValidPwd && isValidPwdCheck && isValidName && isValidPhone && isValidEmail && isValidBirth) {
    this.submit();
  }
});


//== 필수사항 =================================
// 1.아이디 유효성 검사 & 중복 체크 (AJAX 요청)
async function validateId() {
  const inputId = document.getElementById('id');
  const errorElement = document.getElementById('idError');

  // 필수 입력
  if (!inputId.value.trim()) {
    showError(errorElement, '필수 정보입니다.', inputId);
    return false;
  }

  // 정규식 검사
  // 1) 길이와 문자 유형
  const regex = /^[a-zA-Z0-9_-]{6,20}$/; // 영문 소문자, 숫자, 특수기호(_, -)만 허용
  if (!regex.test(inputId.value)) {
    showError(errorElement, '아이디는 6~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.', inputId);
    return false;
  }
  // 2) 숫자만 X / 특수문자 맨 앞에 X
  if (/^\d+$/.test(inputId.value) || /^[^a-zA-Z]/.test(inputId.value)) {
    showError(errorElement, '사용할 수 없는 아이디입니다. 다른 아이디를 입력해 주세요.', inputId);
    return false;
  }


  // 유효성 검사 통과 후
  clearError(errorElement, inputId);

  try {
    // 중복 체크(AJAX 요청)
    const response = await fetch(`/members/checkId?memId=${encodeURIComponent(inputId.value)}`);
    //console.log(response);

    if (!response.ok) {
      throw new Error('Request failed');
    }

    const data = await response.json();
    //console.log(data);

    if (data.isDuplicate) {
      showError(errorElement, '사용할 수 없는 아이디입니다. 다른 아이디를 입력해 주세요.', inputId);
    } else {
      clearError(errorElement, inputId);
      return true;
    }
  } catch (error) {
    console.error('Error:', error);
    showError(errorElement, '서버 오류 발생', inputId);
    return false;
  }
};

// 2.비밀번호 유효성 검사
function validatePwd() {
  const inputPwd = document.getElementById('pwd');
  const errorElement = document.getElementById('pwdError');

  if (!inputPwd.value.trim()) {
    showError(errorElement, '필수 정보입니다.', inputPwd);
    return false;
  }

  const regex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+[\]{};':"\\|,.<>?])[A-Za-z\d!@#$%^&*()_+[\]{};':"\\|,.<>?]{8,16}$/;
  if (!regex.test(inputPwd.value)) {
    showError(errorElement, '8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요. ( 각각 최소 한 개 이상 포함 )', inputPwd);
    return false;
  }

  clearError(errorElement, inputPwd);
  return true;
};

// 3.비밀번호 확인
function validatePwdCheck() {
  const inputPwdCheck = document.getElementById('pwdCheck');
  const pwd = document.getElementById('pwd');
  const errorElement = document.getElementById('pwdError');

  if (!inputPwdCheck.value.trim()) {
    showError(errorElement, '비밀번호를 확인해 주세요.', inputPwdCheck);
    return false;
  }

  if (pwd.value !== inputPwdCheck.value) {
    showError(errorElement, '비밀번호가 일치하지 않습니다.', inputPwdCheck);
    return false;
  }

  clearError(errorElement, inputPwdCheck);
  return true;
}

// 4.이름
function validateName() {
  const inputName = document.getElementById('name');
  const errorElement = document.getElementById('nameError');

  if (!inputName.value.trim()) {
    showError(errorElement, '필수 정보입니다.', inputName);
    return false;
  }

  clearError(errorElement, inputName);
  return true;
}

// 5.휴대전화
function validatePhone() {
  const inputPhone = document.getElementById('phone');
  const errorElement = document.getElementById('phoneError');

  if (!inputPhone.value.trim()) {
    showError(errorElement, '필수 정보입니다.', inputPhone);
    return false;
  }
  
  //숫자만 가능 / 시작 0만 가능 / 10~11자리
  const regex = /^0\d{9,10}$/;
  if (!regex.test(inputPhone.value)) {
    showError(errorElement, '휴대전화번호가 정확한지 확인해 주세요.', inputPhone);
    return false;
  }

  clearError(errorElement, inputPhone);
  return true;
}

//== 선택사항 =================================
//값 입력되어 있는 경우에만 유효성 검사 실시

// 1.이메일
function validateEmail() {
  const inputEmail = document.getElementById('email');
  const errorElement = document.getElementById('emailError');

  if (inputEmail.value.trim()) {
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!emailPattern.test(inputEmail.value)) {
      showError(errorElement, '이메일 주소가 정확한지 확인해 주세요.', inputEmail);
      return false;
    }
  }
  clearError(errorElement, inputEmail);
  return true;
}

// 2.생년월일
function validateBirth() {
  const inputBirth = document.getElementById('birth');
  const errorElement = document.getElementById('birthError');

  if (inputBirth.value.trim()) {
    //형식
    const regex = /^\d{8}$/;
    if (!regex.test(inputBirth.value)) {
      showError(errorElement, '생년월일은 8자리 숫자로 입력해 주세요.', inputBirth);
      return false;
    }

    //날짜 유효성
    const year = parseInt(inputBirth.value.substring(0, 4), 10);
    const month = parseInt(inputBirth.value.substring(4, 6), 10);
    const day = parseInt(inputBirth.value.substring(6, 8), 10);
    const date = new Date(year, month -1, day);

    // 1910.01.01 이후 부터 가능
    if (year < 1910 || date.getFullYear() !== year || date.getMonth() + 1 !== month || date.getDate() !== day) {
      showError(errorElement, '생년월일이 정확한지 확인해 주세요.', inputBirth);
      return false;
    }

    // 나이 제한 검사 (14세 이상)
    const today = new Date();
    const age = today.getFullYear() - year - (today.getMonth() < month - 1 || (today.getMonth() === month - 1 && today.getDate() < day) ? 1 : 0);

    if (age < 14) {
      showError(errorElement, '만 14세 미만의 아동은 회원가입 시 법적대리인의 동의가 필요합니다.\n회원가입이 필요할 시 당사 고객센터로 연락해주세요.', inputBirth);
      return false;
    }
  }

  clearError(errorElement, inputBirth);
  return true;
}


//== method ==============================================================
// 에러 메시지 출력 함수
function showError(errorElement, message, inputElement) {
  errorElement.textContent = message;
  errorElement.classList.add('visible'); //오류메시지 display

  if (inputElement) { // inputElement 값 넘어올 때만 input박스 색상 적용
    inputElement.classList.toggle('input-error');
  }

}

// 에러 메시지 제거 함수
function clearError(errorElement, inputElement) {
  errorElement.textContent = '';
  errorElement.classList.remove('visible');
  inputElement.classList.remove('input-error');
}


