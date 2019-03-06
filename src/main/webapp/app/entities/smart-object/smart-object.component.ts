import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISmartObject } from 'app/shared/model/smart-object.model';
import { AccountService } from 'app/core';
import { SmartObjectService } from './smart-object.service';

@Component({
    selector: 'jhi-smart-object',
    templateUrl: './smart-object.component.html'
})
export class SmartObjectComponent implements OnInit, OnDestroy {
    smartObjects: ISmartObject[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected smartObjectService: SmartObjectService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.smartObjectService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<ISmartObject[]>) => (this.smartObjects = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.smartObjectService.query().subscribe(
            (res: HttpResponse<ISmartObject[]>) => {
                this.smartObjects = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSmartObjects();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISmartObject) {
        return item.id;
    }

    registerChangeInSmartObjects() {
        this.eventSubscriber = this.eventManager.subscribe('smartObjectListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
