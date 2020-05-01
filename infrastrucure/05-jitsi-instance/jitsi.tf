locals {
  zone_id = var.stage == "prod" ? data.terraform_remote_state.aws_dns.outputs.meissa_prod_zone_id : data.terraform_remote_state.aws_dns.outputs.meissa_test_zone_id
}

resource "hcloud_server" "jitsi" {
  name        = "${var.module}.${var.stage}.meissa"
  image       = "ubuntu-18.04"
  server_type = "cx31"
  ssh_keys    = [hcloud_ssh_key.jitsi_ssh_key.id]
}

resource "aws_route53_record" "v4" {
  zone_id = local.zone_id
  name    = "${var.module}"
  type    = "A"
  ttl     = "300"
  records = [hcloud_server.jitsi.ipv4_address]
}

resource "aws_route53_record" "v6" {
  zone_id = local.zone_id
  name    = "${var.module}-neu"
  type    = "AAAA"
  ttl     = "300"
  records = ["${hcloud_server.jitsi.ipv6_address}1"]
}

output "ipv4" {
  value = hcloud_server.jitsi.ipv4_address
}

output "ipv6" {
  value = hcloud_server.jitsi.ipv6_address
}

output "fqdn" {
  value = aws_route53_record.v4.fqdn
}
