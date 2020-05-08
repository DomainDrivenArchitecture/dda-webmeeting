resource "aws_key_pair" "deployer" {
  key_name   = "deployer-key2"
  public_key = file("${var.path_to_ssh_pub}")
}