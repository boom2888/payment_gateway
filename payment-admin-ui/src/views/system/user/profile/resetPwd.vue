<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item :label="$t('system.profile.oldPassword')" prop="oldPassword">
      <el-input v-model="user.oldPassword" :placeholder="$t('system.profile.pleaseEnterOldPassword')" type="password" />
    </el-form-item>
    <el-form-item :label="$t('system.profile.newPassword')" prop="newPassword">
      <el-input v-model="user.newPassword" :placeholder="$t('system.profile.pleaseEnterNewPassword')" type="password" />
    </el-form-item>
    <el-form-item :label="$t('system.profile.confirmPassword')" prop="confirmPassword">
      <el-input
        v-model="user.confirmPassword"
        :placeholder="$t('system.profile.pleaseConfirmPassword')"
        type="password"
      />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">{{ $t('system.profile.save') }}</el-button>
      <el-button type="danger" size="mini" @click="close">{{ $t('system.profile.close') }}</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserPwd } from '@/api/system/user'

export default {
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.user.newPassword !== value) {
        callback(new Error(this.$t('system.profile.passwordNotMatch')))
      } else {
        callback()
      }
    }
    return {
      test: '1test',
      user: {
        oldPassword: undefined,
        newPassword: undefined,
        confirmPassword: undefined
      },
      // 表单校验
      rules: {
        oldPassword: [{ required: true, message: this.$t('system.profile.oldPasswordRequired'), trigger: 'blur' }],
        newPassword: [
          { required: true, message: this.$t('system.profile.newPasswordRequired'), trigger: 'blur' },
          { min: 6, max: 20, message: this.$t('system.profile.passwordLength'), trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: this.$t('system.profile.confirmPasswordRequired'), trigger: 'blur' },
          { required: true, validator: equalToPassword, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submit() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          updateUserPwd(this.user.oldPassword, this.user.newPassword).then(response => {
            this.msgSuccess(this.$t('system.profile.modifySuccess'))
          })
        }
      })
    },
    close() {
      this.$store.dispatch('tagsView/delView', this.$route)
      this.$router.push({ path: '/index' })
    }
  }
}
</script>
