data "terraform_remote_state" "aws_dns" {
  backend   = "s3"

  config = {
    region     = var.region
    bucket     = var.bucket
    key        = "${var.account_name}/dns"
    kms_key_id = var.kms_key_id
    encrypt    = true
  }
}
