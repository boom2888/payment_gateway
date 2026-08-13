<template>
  <div class="register-container">
    <div class="language-switch-wrapper">
      <language-switch class="language-switch-btn"/>
    </div>
    <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form" label-width="120px">
      <h3 class="title">{{ $t('common.register') }}</h3>

      <el-form-item :label="$t('common.businessName')" prop="businessName">
        <el-input v-model="registerForm.businessName" :placeholder="$t('common.pleaseEnterBusinessName')"/>
      </el-form-item>

      <el-form-item :label="$t('common.loginUserName')" prop="userName">
        <el-input v-model="registerForm.userName" :placeholder="$t('common.pleaseEnterLoginUserName')"/>
      </el-form-item>

      <el-form-item :label="$t('common.loginPassword')" prop="password">
        <el-input
            v-model="registerForm.password"
            type="password"
            :placeholder="$t('common.pleaseEnterLoginPassword')"
        />
      </el-form-item>

      <el-form-item :label="$t('common.confirmPassword')" prop="confirmPassword">
        <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            :placeholder="$t('common.pleaseConfirmPassword')"
        />
      </el-form-item>

      <el-form-item :label="$t('common.mobile')" prop="mobile">
        <el-input v-model="registerForm.mobile" :placeholder="$t('common.pleaseEnterMobile')"/>
      </el-form-item>

      <el-form-item>
        <el-button :loading="loading" type="primary" style="width: 100%" @click.native.prevent="handleRegister">
          {{ $t('common.register') }}
        </el-button>
        <div style="margin-top: 20px">
          <router-link to="/login" class="link-type">{{ $t('common.hasAccount') }}</router-link>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {registerShopUser} from '@/api/system/user'
import LanguageSwitch from '@/components/LanguageSwitch'

export default {
  name: 'Register',
  components: {
    LanguageSwitch
  },
  data() {
    const validateUserName = (rule, value, callback) => {
      if (value.length < 2) {
        callback(new Error(this.$t('common.accountMinLength')))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error(this.$t('common.passwordMinLength')))
      } else {
        callback()
      }
    }
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.registerForm.password) {
        callback(new Error(this.$t('common.passwordMismatch')))
      } else {
        callback()
      }
    }
    return {
      registerForm: {
        businessName: '',
        userName: '',
        mobile: '',
        password: '',
        confirmPassword: '',
        type: 1 // 1:后台商户,2: emi 商户
      },
      registerRules: {
        businessName: [{required: true, trigger: 'blur', message: this.$t('common.pleaseEnterBusinessName')}],
        userName: [
          {required: true, trigger: 'blur', message: this.$t('common.pleaseEnterAccount')},
          {validator: validateUserName, trigger: 'blur'}
        ],
        mobile: [
          {required: true, trigger: 'blur', message: this.$t('common.pleaseEnterMobile')},
          {pattern: /^1[3-9]\d{9}$/, trigger: 'blur', message: this.$t('common.pleaseEnterCorrectMobile')}
        ],
        password: [
          {required: true, trigger: 'blur', message: this.$t('common.pleaseEnterPassword')},
          {validator: validatePassword, trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, trigger: 'blur', message: this.$t('common.pleaseConfirmPassword')},
          {validator: validateConfirmPassword, trigger: 'blur'}
        ]
      },
      loading: false
    }
  },
  methods: {
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true
          registerShopUser(this.registerForm)
              .then(response => {
                if (response.code === 200) {
                  this.$message({
                    message: this.$t('common.registerSuccess'),
                    type: 'success',
                    duration: 1500,
                    onClose: () => {
                      this.$router.push('/login')
                    }
                  })
                } else {
                  this.$message.error(response.msg || this.$t('common.registerFailed'))
                  this.loading = false
                }
              })
              .catch(error => {
                // this.$message.error(error.message || this.$t('common.registerFailed'))
                // this.loading = false
              })
              .finally(() => {
                if (this.loading) {
                  this.loading = false
                }
              })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f3f3f3;

  .register-form {
    width: 520px;
    padding: 35px;
    border-radius: 10px;
    background: #ffffff;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .title {
      text-align: center;
      margin: 0 auto 30px;
      color: #333;
    }

    .link-type {
      color: #409eff;
      text-decoration: none;
      font-size: 14px;

      &:hover {
        color: #66b1ff;
      }
    }
  }
}

.language-switch-wrapper {
  position: fixed;
  top: 30px;
  right: 40px;
  z-index: 9999;

  .language-switch-btn {
    background: rgba(255, 255, 255, 0.9);
    backdrop-filter: blur(10px);
    border-radius: 20px;
    padding: 8px 16px;
    transition: all 0.3s ease;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

    &:hover {
      background: rgba(255, 255, 255, 1);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }
  }
}
</style>
