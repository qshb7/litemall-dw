import request from '@/utils/request'

const AppraiseBadTopNList = '/dw/adsAppraiseBadTopn/list'
export function listAppraiseBadTopn(query) {
  return request({
    url: AppraiseBadTopNList,
    method: 'get',
    params: query
  })
}

const AdsDateTopicList = '/dw/dashBoard/list'
export function listAdsDateTopic(query) {
  return request({
    url: AdsDateTopicList,
    method: 'get',
    params: query
  })
}

const AdsDateTopicChart = '/dw/dashBoard/chart'
export function chartDuration(query) {
  return request({
    url: AdsDateTopicChart,
    method: 'get',
    params: query
  })
}

const RegionOrderList = '/dw/report/region'
export function chartRegion(query) {
  return request({
    url: RegionOrderList,
    method: 'get',
    params: query
  })
}

const UAConvertList = '/dw/report/convert'
export function uaConvertList(query) {
  return request({
    url: UAConvertList,
    method: 'get',
    params: query
  })
}

const MapJsonData = '/dw/map/json/'
export function mapJson(id) {
  return request({
    url: MapJsonData + id,
    method: 'get'
  })
}

const SaleTopN = '/dw/report/sale'
export function saleTopN(query) {
  return request({
    url: SaleTopN,
    method: 'get',
    params: query
  })
}
